package server.net.message.in;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.util.StreamBuffer.InBuffer;

public interface MessageDecoder {

	public void decode(Player player, InBuffer in, ReceivedPacket packet);
	
}
