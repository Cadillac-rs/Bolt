package server.net.message.in.impl;

import java.util.Arrays;

import server.game.Position;
import server.game.item.Item;
import server.game.player.Player;
import server.game.player.PlayerHandler;
import server.game.player.skill.Skill;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;

/**
 * 
 * @author Admin
 *
 */
public class CommandMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 103: // Player command.
		String command = in.readString();
		String[] split = command.split(" ");
		String keyword = split[0];
		//handleCommand(split[0].toLowerCase(), Arrays.copyOfRange(split, 1, split.length));
	
		if(keyword.equals("players")) {
			int count = 0;
			for(Player p : PlayerHandler.getPlayers()) {
				if(player != null)
					count++;
			}
			player.getEncoder().sendMessage("Players online: " + count);
		}
		if (keyword.equals("master")) {
			for (int i = 0; i < player.getSkills().getSkills().length; i++) {
				for (Skill s : player.getSkills().getSkills()) {
					s.setRealLevelAndExperience(99, true);
				}
			}
			player.sendSkills();
		}
		if (keyword.equals("noob")) {
			for (int i = 0; i < player.getSkills().getSkills().length; i++) {
				player.getSkills().getSkills()[i].setRealLevelAndExperience(i == 3 ? 10 : 1, true);
			}
			player.sendSkills();
		}
		if (keyword.equals("empty")) {
			player.emptyInventory();
		}
		if (keyword.equals("pickup")) {
			int id = Integer.parseInt(split[0]);
			int amount = 1;
			if (split.length > 1) {
				amount = Integer.parseInt(split[1]);
			}
			player.addInventoryItem(new Item(id, amount));
			player.sendInventory();
		}
		if (keyword.equals("tele")) {
			int x = Integer.parseInt(split[1]);
			int y = Integer.parseInt(split[2]);
			player.teleport(new Position(x, y, player.getPosition().getZ()));
		}
		if (keyword.equals("mypos")) {
			player.getEncoder().sendMessage("You are at: " + player.getPosition());
		}
		
	}

}
