package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer;
import server.net.util.StreamBuffer.InBuffer;

/**
 * Packet 41 triggered when a player attempts to equip an item.
 *  
 * @author Admin
 */
public final class EquipItemMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 41: // Equip item.
		in.readShort(); // Item ID.
		int slot = in.readShort(StreamBuffer.ValueType.A); // inventory slot
		in.readShort(); // Interface ID.
		

	}

}
