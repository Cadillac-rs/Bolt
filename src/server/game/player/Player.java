package server.game.player;
/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jboss.netty.channel.Channel;

import server.game.Position;
import server.game.item.Item;
import server.game.npc.Npc;
import server.game.player.skill.SkillSet;
import server.game.world.Positionable;
import server.game.world.Task;
import server.game.world.entity.Entity;
import server.net.ReceivedPacket;
import server.net.message.MessageHandler;
import server.net.util.ISAACCipher;
import server.net.util.StreamBuffer;
import server.util.Misc;

/**
 * Represents a logged-in 
 * 
 * @author blakeman8192
 */
public class Player extends Entity {
	
	private int pid;
	
	private final String username;
	private final String password;
	
	private ISAACCipher encryptor;
	private final Channel channel;
	
	private final PacketEncoder encoder = new PacketEncoder(this);
	
	private final Map<Integer, Player> players = new HashMap<Integer, Player>();
	private final List<Npc> npcs = new LinkedList<Npc>();
	
	/**
	 * The object containing the player's skills.
	 */
	private final SkillSet skills = new SkillSet();
	
	private int primaryDirection = -1;
	private int secondaryDirection = -1;
	private int staffRights = 0;
	private int chatColor;
	private int chatEffect;
	private byte[] chatText;
	private int gender = Misc.GENDER_MALE;
	private final int[] appearance = new int[7];
	private final int[] colors = new int[5];
	
	// player items
	private Item[] equipment = new Item[PlayerConstants.EQUIPMENT_SIZE];
	private Item[] inventory = new Item[PlayerConstants.INVENTORY_SIZE];
	
	// friends ignores
	private List<Long> friends = new ArrayList<Long>();
	private List<Long> ignores = new ArrayList<Long>();
	
	// Client settings
	private byte brightness = 1;
	private boolean mouseButtons;
	private boolean splitScreen = false;
	private boolean acceptAid = false;
	private boolean retaliate = false;
	private boolean chatEffects = false;
	private byte publicChat = 0;
	private byte privateChat = 0;
	private byte tradeCompete = 0;
	
	// Various player update flags.
	private boolean updateRequired = false;
	private boolean appearanceUpdateRequired = false;
	private boolean chatUpdateRequired = false;
	private boolean needsPlacement = false;
	private boolean resetMovementQueue = false;

	private final Queue<ReceivedPacket> queuedPackets = new ConcurrentLinkedQueue<ReceivedPacket>();

	/**
	 * Adds a packet to the queue
	 * @param packet
	 */
	public void queuePacket(ReceivedPacket packet) {
		queuedPackets.add(packet);
	}
	
	/**
	 * Handles packets we have received
	 */
	public void processQueuedPackets() {
		ReceivedPacket packet = null;
		while((packet = queuedPackets.poll()) != null) {
			MessageHandler.decode(this, StreamBuffer.OutBuffer.newInBuffer(packet.getPayload()), packet);
		}
	}
	
	/**
	 * Sends all skills to the client.
	 */
	public void sendSkills() {
		for (int i = 0; i < skills.getSkills().length; i++) {
			encoder.sendSkill(i, skills.getSkills()[i].getLevel(), (int)skills.getSkills()[i].getExperience());
		}
	}

	/**
	 * Sends all equipment.
	 */
	public void sendEquipment() {
		for (int i = 0; i < PlayerConstants.EQUIPMENT_SIZE; i++) {
			Item item = getEquipment()[i];
			if(item != null) {
				encoder.sendEquipment(i, item.getId(), item.getAmount());
			} else {
				encoder.sendEquipment(i, -1, 0);
			}
		}
	}

	/**
	 * Sends the current full inventory.
	 */
	public void sendInventory() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(256);
		out.writeVariableShortPacketHeader(getEncryptor(), 53);
		out.writeShort(3214);
		out.writeShort(PlayerConstants.INVENTORY_SIZE);
		for (int i = 0; i < PlayerConstants.INVENTORY_SIZE; i++) {
			Item item = getInventory()[i];
			if(item != null && item.getAmount() > 254) {
				out.writeByte(255);
				out.writeInt(item.getAmount(), StreamBuffer.ByteOrder.INVERSE_MIDDLE);
			} else {
				out.writeByte(item != null ? item.getAmount() : 0);
			}
			out.writeShort(item != null ? item.getId() + 1 : 0, StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		}
		out.finishVariableShortPacketHeader();
		encoder.send(out.getBuffer());
	}

	public boolean hasFriend(long friend) {
		return getFriends().contains(friend);
	}
	
	public PacketEncoder getEncoder() {
		return encoder;
	}

	/**
	 * Creates a new Player.
	 * 
	 * @param key
	 *            the SelectionKey
	 */
	public Player(Channel channel, String username, String password) {
		this.channel = channel;
		this.username = username;
		this.password = password;
		
		// Set the default appearance.
		getAppearance()[Misc.APPEARANCE_SLOT_CHEST] = 18;
		getAppearance()[Misc.APPEARANCE_SLOT_ARMS] = 26;
		getAppearance()[Misc.APPEARANCE_SLOT_LEGS] = 36;
		getAppearance()[Misc.APPEARANCE_SLOT_HEAD] = 0;
		getAppearance()[Misc.APPEARANCE_SLOT_HANDS] = 33;
		getAppearance()[Misc.APPEARANCE_SLOT_FEET] = 42;
		getAppearance()[Misc.APPEARANCE_SLOT_BEARD] = 10;

		// Set the default colors.
		getColors()[0] = 7;
		getColors()[1] = 8;
		getColors()[2] = 9;
		getColors()[3] = 5;
		getColors()[4] = 0;
			
	}

	/**
	 * Performs processing for this 
	 * 
	 * @throws Exception
	 */
	public void process() {
		getMovementHandler().process();
		
		getTasks().tick();
	}

	/**
	 * Equips an item.
	 * 
	 * @param slot
	 *            the inventory slot
	 */
	public void equip(int slot) {
		Item item = getInventory()[slot];
		
		if(item == null) {
			return;
		}
		
		int eSlot = item.getDefinition().getSlot();
		if(eSlot == -1) {
			return;
		}
		
		if(getEquipment()[eSlot] != null) {
			unequip(eSlot);
		}
		
		getInventory()[slot] = null;
		getEquipment()[eSlot] = item;
		
		encoder.sendEquipment(eSlot, item.getId(), item.getAmount());
		sendInventory();
		setAppearanceUpdateRequired(true);
	}

	/**
	 * Unequips an item.
	 * 
	 * @param slot
	 *            the equipment slot.
	 */
	public void unequip(int slot) {
		Item item = getEquipment()[slot];
		if(addInventoryItem(item)) {
			getEquipment()[slot] = null;
			encoder.sendEquipment(slot, -1, 0);
			sendInventory();
			setAppearanceUpdateRequired(true);
		}
	}

	/**
	 * Empties the entire inventory.
	 */
	public void emptyInventory() {
		for(int i = 0; i < PlayerConstants.EQUIPMENT_SIZE; i++) {
			getEquipment()[i] = null;
		}
		sendInventory();
	}

	/**
	 * Attempts to add the item (and amount) to the inventory. This method will
	 * add as many of the desired item to the inventory as possible, even if not
	 * all can be added.
	 * 
	 * @param id
	 *            the item ID
	 * @param amount
	 *            the amount of the item
	 * @return whether or not the amount of the item could be added to the
	 *         inventory
	 */
	public boolean addInventoryItem(Item item) {
		if (item.getDefinition().isStackable()) {
			
			// Add the item to an existing stack if there is one.
			for(int i = 0; i < PlayerConstants.INVENTORY_SIZE; i++) {
				Item inventItem = getInventory()[i];
				if(inventItem != null && inventItem.getId() == item.getId()) {
					inventItem.incrementAmountBy(item.getAmount());
					return true;
				}
			}
			
			// No stack, try to add the item stack to an empty slot.
			for(int i = 0; i < PlayerConstants.INVENTORY_SIZE; i++) {
				Item inventItem = getInventory()[i];
				if(inventItem == null) {
					getInventory()[i] = item;
					return true;
				}		
			}
		} else {
			// Try to add the amount of items to empty slots.
			int amountAdded = 0;
			for(int i = 0; i < PlayerConstants.INVENTORY_SIZE && amountAdded < item.getAmount(); i++) {
				Item inventItem = getInventory()[i];
				if(inventItem == null) {
					getInventory()[i] = new Item(item.getId(), 1);
					amountAdded++;
				}		
			}
			
			// Check we added all of the items
			if(amountAdded >= item.getAmount()) {
				return true;
			}
		}
		encoder.sendMessage("You do not have enough inventory space.");
		return false;
	}

	/**
	 * Removes the desired amount of the specified item from the inventory.
	 * 
	 * @param id
	 *            the item ID
	 * @param amount
	 *            the desired amount
	 */
	public void removeInventoryItem(Item item) {
		if (item.getDefinition().isStackable()) {
			// Find the existing stack (if there is one).
			for(int i = 0 ; i < PlayerConstants.INVENTORY_SIZE; i++) {
				Item inventItem = getInventory()[i];
				if(inventItem != null && inventItem.getId() == item.getId()) {
					if(item.getAmount() >= inventItem.getAmount()) {
						getInventory()[i] = null;
						break;
					}
				}
			}
		} else {
			// Remove the desired amount.
			int amountRemoved = 0;
			for (int i = 0; i < PlayerConstants.INVENTORY_SIZE && amountRemoved < item.getAmount(); i++) {
				Item inventItem = getInventory()[i];
				if(inventItem != null && inventItem.getId() == item.getId()) {
					getInventory()[i] = null;
					amountRemoved++;
				}
			}
		}
		sendInventory();
	}

	/**
	 * Checks if the desired amount of the item is in the inventory.
	 * 
	 * @param id
	 *            the item ID
	 * @param amount
	 *            the item amount
	 * @return whether or not the player has the desired amount of the item in
	 *         the inventory
	 */
	public boolean hasInventoryItem(Item item) {
		if (item.getDefinition().isStackable()) {
			// Check if an existing stack has the amount of item.
			for(int i = 0 ; i < PlayerConstants.INVENTORY_SIZE; i++) {
				Item inventItem = getInventory()[i];
				if(inventItem != null && inventItem.getId() == item.getId()) {
					return inventItem.getAmount() >= item.getAmount();
				}
			}
		} else {
			// Check if there are the amount of items.
			int amountFound = 0;
			for(int i = 0 ; i < PlayerConstants.INVENTORY_SIZE; i++) {
				Item inventItem = getInventory()[i];
				if(inventItem != null && inventItem.getId() == item.getId()) {
					amountFound++;
				}
			}
			return amountFound >= item.getAmount();
		}
		return false;
	}

	/**
	 * Teleports the player to the desired position.
	 * 
	 * @param position
	 *            the position
	 */
	public void teleport(Position position) {
		getMovementHandler().reset();
		getPosition().setAs(position);
		setResetMovementQueue(true);
		setNeedsPlacement(true);
		encoder.sendMapRegion();
	}

	/**
	 * Resets the player after updating.
	 */
	public void reset() {
		setPrimaryDirection(-1);
		setSecondaryDirection(-1);
		setUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		setChatUpdateRequired(false);
		setResetMovementQueue(false);
		setNeedsPlacement(false);
	}

	public void login() {
		int response = Misc.LOGIN_RESPONSE_OK;

		// Check if the player is already logged in.
		for (Player player : PlayerHandler.getPlayers()) {
			if (player == null) {
				continue;
			}
			if (username.equals(player.getUsername())) {
				System.err.println("R");
				response = Misc.LOGIN_RESPONSE_ACCOUNT_ONLINE;
			}
		}

		// Load the player and send the login response.
		int status = 0;
		
		if(status == 1) {
			response = Misc.LOGIN_RESPONSE_NEED_MEMBERS;
		} else if(status == 2) {
			response = Misc.LOGIN_RESPONSE_INVALID_CREDENTIALS;
		}
		
		StreamBuffer.OutBuffer resp = StreamBuffer.newOutBuffer(3);
		resp.writeByte(response);
		resp.writeByte(getStaffRights());
		resp.writeByte(0);
		encoder.send(resp.getBuffer());
		if (response != 2) {
			disconnect();
			return;
		}

		PlayerHandler.register(this);
		encoder.sendMapRegion();
		sendInventory();
		sendSkills();
		sendEquipment();
		setUpdateRequired(true);
		setAppearanceUpdateRequired(true);
		encoder.sendSidebarInterface(1, 3917);
		encoder.sendSidebarInterface(2, 638);
		encoder.sendSidebarInterface(3, 3213);
		encoder.sendSidebarInterface(4, 1644);
		encoder.sendSidebarInterface(5, 5608);
		encoder.sendSidebarInterface(6, 1151);
		encoder.sendSidebarInterface(8, 5065);
		encoder.sendSidebarInterface(9, 5715);
		encoder.sendSidebarInterface(10, 2449);
		encoder.sendSidebarInterface(11, 4445);
		encoder.sendSidebarInterface(12, 147);
		encoder.sendSidebarInterface(13, 6299);
		encoder.sendSidebarInterface(0, 2423);
		encoder.sendConfig(166, getBrightness() + 1);
		encoder.sendConfig(287, (splitScreen() ? 1 : 0));
		encoder.sendConfig(170, (mouseButtons() ? 1 : 0));
		encoder.sendConfig(171, (!chatEffects() ? 1 : 0));
		encoder.sendConfig(427, (acceptAid() ? 1 : 0));
		encoder.sendConfig(173, (getMovementHandler().isRunToggled() ? 1 : 0));
		encoder.sendConfig(172, (retaliate() ? 0 : 1));
		encoder.sendChatOptions();
		encoder.sendFriendsListUpdate();
		encoder.sendFriendsList();
		encoder.sendIgnoreList();
		encoder.updateOtherFriends(getPrivateChat());
		
		encoder.sendMessage("Welcome to RuneSource!");

		getTasks().submit(new Task(5, false) {
			
			int cycle = 0;
			
			@Override
			public void execute() {
				if (cycle == 0) {
					getEncoder().sendMessage("Hey!");
				} else {
					getEncoder().sendMessage("This is a timed message.");
				}
				cycle++;
			}
			
		});
		
		//System.out.println(this + " has logged in.");
	}

	/**
	 * Disconnects the client.
	 */
	public void disconnect() {
		System.out.println(this + " disconnecting.");
		try {
			logout();
			channel.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void logout() throws Exception {
		encoder.updateOtherFriends(2);
		PlayerHandler.unregister(this);
		System.out.println(this + " has logged out.");
		if (getSlot() != -1) {
			//SaveLoad.save(this);
		}
	}

	public String toString() {
		return username == null ? "Host(" + getHost() + ")" : "Player(" + username + ":" + password + " - " + getHost() + ")";
	}

	/**
	 * Sets the player's primary movement direction.
	 * 
	 * @param primaryDirection
	 *            the direction
	 */
	public void setPrimaryDirection(int primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	/**
	 * Gets the player's primary movement direction.
	 * 
	 * @return the direction
	 */
	public int getPrimaryDirection() {
		return primaryDirection;
	}

	/**
	 * Sets the player's secondary movement direction.
	 * 
	 * @param secondaryDirection
	 *            the direction
	 */
	public void setSecondaryDirection(int secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	/**
	 * Gets the player's secondary movement direction.
	 * 
	 * @return the direction
	 */
	public int getSecondaryDirection() {
		return secondaryDirection;
	}

	/**
	 * Sets the needsPlacement boolean.
	 * 
	 * @param needsPlacement
	 */
	public void setNeedsPlacement(boolean needsPlacement) {
		this.needsPlacement = needsPlacement;
	}

	/**
	 * Gets whether or not the player needs to be placed.
	 * 
	 * @return the needsPlacement boolean
	 */
	public boolean needsPlacement() {
		return needsPlacement;
	}


	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public boolean isUpdateRequired() {
		return updateRequired;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		if (appearanceUpdateRequired) {
			setUpdateRequired(true);
		}
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setStaffRights(int staffRights) {
		this.staffRights = staffRights;
	}

	public int getStaffRights() {
		return staffRights;
	}

	public void setResetMovementQueue(boolean resetMovementQueue) {
		this.resetMovementQueue = resetMovementQueue;
	}

	public boolean isResetMovementQueue() {
		return resetMovementQueue;
	}

	public void setChatColor(int chatColor) {
		this.chatColor = chatColor;
	}

	public int getChatColor() {
		return chatColor;
	}

	public void setChatEffects(int chatEffects) {
		this.chatEffect = chatEffects;
	}

	public int getChatEffects() {
		return chatEffect;
	}

	public void setChatText(byte[] chatText) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatUpdateRequired(boolean chatUpdateRequired) {
		if (chatUpdateRequired) {
			setUpdateRequired(true);
		}
		this.chatUpdateRequired = chatUpdateRequired;
	}

	public boolean isChatUpdateRequired() {
		return chatUpdateRequired;
	}

	public int[] getAppearance() {
		return appearance;
	}

	public int[] getColors() {
		return colors;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public int getGender() {
		return gender;
	}

/*	public List<Player> getPlayers() {
		return players;
	} */

	public List<Npc> getNpcs() {
		return npcs;
	}

	public Map<Integer, Player> getPlayers() {
		return players;
	}

	public byte getBrightness() {
		return brightness;
	}

	public void setBrightness(byte brightness) {
		this.brightness = brightness;
	}

	public boolean mouseButtons() {
		return mouseButtons;
	}

	public void setMouseButtons(boolean mouseButtons) {
		this.mouseButtons = mouseButtons;
	}

	public boolean splitScreen() {
		return splitScreen;
	}

	public void setSplitScreen(boolean splitScreen) {
		this.splitScreen = splitScreen;
	}

	public boolean acceptAid() {
		return acceptAid;
	}

	public void setAcceptAid(boolean acceptAid) {
		this.acceptAid = acceptAid;
	}

	public boolean retaliate() {
		return retaliate;
	}

	public void setRetaliate(boolean retaliate) {
		this.retaliate = retaliate;
	}

	public boolean chatEffects() {
		return chatEffects;
	}

	public void setChatEffects(boolean chatEffects) {
		this.chatEffects = chatEffects;
	}

	public byte getPublicChat() {
		return publicChat;
	}

	public void setPublicChat(byte pubicChat) {
		this.publicChat = pubicChat;
	}

	public byte getTradeCompete() {
		return tradeCompete;
	}

	public void setTradeCompete(byte tradeCompete) {
		this.tradeCompete = tradeCompete;
	}

	public byte getPrivateChat() {
		return privateChat;
	}

	public void setPrivateChat(byte privateChat) {
		this.privateChat = privateChat;
	}

	public List<Long> getFriends() {
		return friends;
	}

	public void setFriends(List<Long> friends) {
		this.friends = friends;
	}

	public List<Long> getIgnores() {
		return ignores;
	}

	public void setIgnores(List<Long> ignores) {
		this.ignores = ignores;
	}
	
	public Item[] getInventory() {
		return inventory;
	}
	
	public Item[] getEquipment() {
		return equipment;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	/**
	 * Gets the remote host of the client.
	 * 
	 * @return the host
	 */
	public String getHost() {
		if(channel == null) {
			return "unknown";
		}
		return ((InetSocketAddress) channel.getRemoteAddress()).getAddress().getHostAddress();
	}

	/**
	 * Sets the encryptor.
	 * 
	 * @param encryptor
	 *            the encryptor
	 */
	public void setEncryptor(ISAACCipher encryptor) {
		this.encryptor = encryptor;
	}

	public SkillSet getSkills() {
		return skills;
	}
	
	/**
	 * Gets the encryptor.
	 * 
	 * @return the encryptor
	 */
	public ISAACCipher getEncryptor() {
		return encryptor;
	}
	
	public Channel getChannel() {
		return channel;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	

}
