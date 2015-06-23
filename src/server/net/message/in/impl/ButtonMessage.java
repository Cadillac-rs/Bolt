package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;
import server.util.Misc;

/**
 * Packet 185 triggered when a player clicks a button in game.
 * 
 * @author Admin
 *
 */
public class ButtonMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 185: // Button clicking.
		int button = Misc.hexToInt(in.readBytes(2)) ;
		

	}

}
