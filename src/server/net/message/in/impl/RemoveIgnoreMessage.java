package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;

/**
 * Packet 74 which is sent when a player removes an ignored player.
 *
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public class RemoveIgnoreMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 74: // Remove ignore
		long ignore = in.readLong();
		if(!player.getIgnores().contains(ignore)) {
			player.getEncoder().sendMessage("That player is not on your ignore list.");
			return;
		}
		player.getIgnores().remove(ignore);
	}
	
	

}
