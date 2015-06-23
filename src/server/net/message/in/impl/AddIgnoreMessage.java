package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;

public class AddIgnoreMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
		//case 133: // Add ignore
		
		long ignore = in.readLong();
		if(player.getIgnores().size() >= 100) {
			player.getEncoder().sendMessage("Ignore list is full.");
			return;
		}
		if(player.getIgnores().contains(ignore)) {
			player.getEncoder().sendMessage("That player is already on your ignore list.");
			return;
		}
		player.getIgnores().add(ignore);

	}

}
