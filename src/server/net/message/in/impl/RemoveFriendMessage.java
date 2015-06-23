package server.net.message.in.impl;

import server.game.player.Player;
import server.game.player.PlayerHandler;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;
import server.util.Misc;

/**
 * Packet 215 which is sent when a player removes a friend.
 *
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public class RemoveFriendMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {

		long friend = in.readLong();
		if(!player.getFriends().contains(friend)) {
			player.getEncoder().sendMessage("That player is not on your friends list.");
			return;
		}
		player.getFriends().remove(friend);
		if(player.getPrivateChat() == 1) {
			Player plr = PlayerHandler.getPlayerByName(Misc.longToName(friend));
			if(plr != null) {
				plr.getEncoder().sendFriendUpdate(Misc.nameToLong(player.getUsername()), (byte)0);
			}
		}
		
	}

	
	
}
