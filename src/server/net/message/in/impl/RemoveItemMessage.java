package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer;
import server.net.util.StreamBuffer.InBuffer;

/**
 * Packet 145 triggered when a {@link Player} attempts to remove
 * an item from an interface.
 *
 */
public class RemoveItemMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	
		int interfaceID = in.readShort(StreamBuffer.ValueType.A);
		int slot = in.readShort(StreamBuffer.ValueType.A);
		in.readShort(StreamBuffer.ValueType.A); // Item ID.
		if (interfaceID == 1688) {
			
			//unequip(slot);
		}
	}

}
