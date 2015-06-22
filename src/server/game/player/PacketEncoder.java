package server.game.player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import org.jboss.netty.buffer.ChannelBuffer;

import server.Server;
import server.net.util.ISAACCipher;
import server.net.util.StreamBuffer;
import server.net.util.StreamBuffer.ByteOrder;
import server.util.Misc;

public class PacketEncoder {

	private final Player player;
	
	public PacketEncoder(Player player) {
		this.player = player;
	}
	
	/**
	 * Tells the client we have logged into the friends server (spoofed)
	 */
	public void sendFriendsListUpdate() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(2);
		out.writeHeader(player.getEncryptor(), 221);
		out.writeByte(2);
		send(out.getBuffer());
	}

	/**
	 * Sends the friends list to the player, really only used when logging in
	 */
	public void sendFriendsList() {
		for(long l : player.getFriends()) {
			if(l == 0) {
				continue;
			}
			byte status = 0;
			Player plr = PlayerHandler.getPlayerByName(Misc.longToName(l));
			if(plr != null) {
				if(plr.getPrivateChat() == 0) {
					status = Server.getSingleton().getWorld();
				} else if(plr.getPrivateChat() == 1) {
					if(plr.hasFriend(Misc.nameToLong(player.getUsername()))) {
						status = Server.getSingleton().getWorld();
					}
				}
			}
			sendFriendUpdate(l, status);
		}
	}
	
	
	
	/**
	 * Tells all your friends that your private chat status has changed.
	 * @param status The status of the players private chat
	 */
	public void updateOtherFriends(int status) {
		long myName = Misc.nameToLong(player.getUsername());
		for(Player plr : PlayerHandler.getPlayers()) {
			if(plr == null || plr == player) {
				continue;
			}
			if(plr.hasFriend(myName)) {
				byte world = 0;
				if(status == 0) {
					world = Server.getSingleton().getWorld();
				} else if(status == 1) {
					if(player.hasFriend(Misc.nameToLong(plr.getUsername()))) {
						world = Server.getSingleton().getWorld();
					}
				}
				plr.getEncoder().sendFriendUpdate(myName, world);
			}
		}
	}
	
	/**
	 * Sends the equipment to the client.
	 * 
	 * @param slot
	 *            the equipment slot
	 * @param itemID
	 *            the item ID
	 * @param itemAmount
	 *            the item amount
	 */
	public void sendEquipment(int slot, int itemID, int itemAmount) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(32);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 34);
		out.writeShort(1688);
		out.writeByte(slot);
		out.writeShort(itemID + 1);
		if (itemAmount > 254) {
			out.writeByte(255);
			out.writeShort(itemAmount);
		} else {
			out.writeByte(itemAmount);
		}
		out.finishVariableShortPacketHeader();
		send(out.getBuffer());
	}
	
	/**
	 * Sends the ignore list to the client
	 */
	public void sendIgnoreList() {
		if(player.getIgnores().size() == 0) {
			return;
		}
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer((player.getIgnores().size() * 8) + 3);
		out.writeVariableShortPacketHeader(player.getEncryptor(), 214);
		for(long i : player.getIgnores()) {
			out.writeLong(i);
		}
		out.finishVariableShortPacketHeader();
		send(out.getBuffer());
	}

	
	/**
	 * Sends a friend update to the friends list indicating which world they are on
	 * @param name The username as a long
	 * @param world The world the player is on
	 */
	public void sendFriendUpdate(long name, byte world) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(10);
		out.writeHeader(player.getEncryptor(), 50);
		out.writeLong(name);
		out.writeByte(world);
		send(out.getBuffer());
	}
	
	/**
	 * Sends a private message
	 * @param name The name of the person sending the message as a long
	 * @param rights Rights of the player
	 * @param message The message itself
	 */
	public void sendPrivateMessage(long name, byte rights, byte[] message) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(15 + message.length);
		out.writeVariablePacketHeader(player.getEncryptor(), 196);
		out.writeLong(name);
		out.writeInt(new Random().nextInt());
		out.writeByte(rights);
		for(int i = 0; i < message.length; i++) {
			out.writeByte(message[i]);
		}
		out.finishVariablePacketHeader();
		send(out.getBuffer());
	}
	
	/**
	 * Sends the skill to the client.
	 * 
	 * @param skillID
	 *            the skill ID
	 * @param level
	 *            the skill level
	 * @param exp
	 *            the skill experience
	 */
	public void sendSkill(int skillID, int level, int exp) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(8);
		out.writeHeader(player.getEncryptor(), 134);
		out.writeByte(skillID);
		out.writeInt(exp, StreamBuffer.ByteOrder.MIDDLE);
		out.writeByte(level);
		send(out.getBuffer());
	}
	
	/**
	 * Sends a packet that tells the client to log out.
	 */
	public void sendLogout() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(1);
		out.writeHeader(player.getEncryptor(), 109);
		send(out.getBuffer());
	}
	
	/**
	 * Sends a configuration update
	 * 
	 * @param id
	 *            The id of the config to update
	 * @param state
	 *            The state to update to
	 */
	public void sendConfig(int id, int state) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 36);
		out.writeShort(id, ByteOrder.LITTLE);
		out.writeByte(state);
		send(out.getBuffer());
	}
	
	/**
	 * Sends a message to the players chat box.
	 * 
	 * @param message
	 *            the message
	 */
	public void sendMessage(String message) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(message.length() + 3);
		out.writeVariablePacketHeader(player.getEncryptor(), 253);
		out.writeString(message);
		out.finishVariablePacketHeader();
		send(out.getBuffer());
	}
	
	/**
	 * Sends the chat options under the chatbox
	 */
	public void sendChatOptions() {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 206);
		out.writeByte(player.getPublicChat());
		out.writeByte(player.getPrivateChat());
		out.writeByte(player.getTradeCompete());
		send(out.getBuffer());
	}

	/**
	 * Sends a sidebar interface.
	 * 
	 * @param menuId
	 *            the interface slot
	 * @param form
	 *            the interface ID
	 */
	public void sendSidebarInterface(int menuId, int form) {
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(4);
		out.writeHeader(player.getEncryptor(), 71);
		out.writeShort(form);
		out.writeByte(menuId, StreamBuffer.ValueType.A);
		send(out.getBuffer());
	}

	/**
	 * Refreshes the map region.
	 */
	public void sendMapRegion() {
		player.getCurrentRegion().setAs(player.getPosition());
		player.setNeedsPlacement(true);
		StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(5);
		out.writeHeader(player.getEncryptor(), 73);
		out.writeShort(player.getPosition().getRegionX() + 6, StreamBuffer.ValueType.A);
		out.writeShort(player.getPosition().getRegionY() + 6);
		send(out.getBuffer());
	}
	
	/**
	 * Sends the buffer to the socket.
	 * 
	 * @param buffer
	 *            the buffer
	 * @throws IOException
	 */
	public void send(ChannelBuffer buffer) {
		if(player.getChannel() == null || !player.getChannel().isConnected()) {
			return;
		}
		player.getChannel().write(buffer);
	}
	
}
