package server.net.message.in.impl;

import java.util.Arrays;

import server.game.player.Player;
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
		//handleCommand(split[0].toLowerCase(), Arrays.copyOfRange(split, 1, split.length));
	
	}

}
