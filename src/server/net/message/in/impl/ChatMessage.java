package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer;
import server.net.util.StreamBuffer.InBuffer;

/**
 * Packet 4 which is executed when a player chats.
 * 
 */
public class ChatMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 4: // Player chat.
		int effects = in.readByte(false, StreamBuffer.ValueType.S);
		int color = in.readByte(false, StreamBuffer.ValueType.S);
		int chatLength = (packet.getSize() - 2);
		byte[] text = in.readBytesReverse(chatLength, StreamBuffer.ValueType.A);
		
		player.setChatEffects(effects);
		player.setChatColor(color);
		player.setChatText(text);
		player.setChatUpdateRequired(true);

		
	}

}
