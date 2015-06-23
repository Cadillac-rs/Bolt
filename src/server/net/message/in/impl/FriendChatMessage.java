package server.net.message.in.impl;

import server.game.player.Player;
import server.game.player.PlayerHandler;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;
import server.util.Misc;

/**
 * Packet 126 sent when a player sends a private message.
 *
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public class FriendChatMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	
		long friend = in.readLong();
		int size = packet.getSize() - 8;
		
		byte[] message = in.readBytes(size);
		
		if(!player.getFriends().contains(friend)) {
			player.getEncoder().sendMessage("That player is not on your friends list.");
			return;
		}
		
		Player plr = PlayerHandler.getPlayerByName(Misc.longToName(friend));
		
		if(plr == null) {
			player.getEncoder().sendMessage("That player is currently offline.");
			return;
		}
		
		plr.getEncoder().sendPrivateMessage(Misc.nameToLong(player.getUsername()), (byte) player.getStaffRights(), message);
		
	}

}
