package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;

/**
 * Packet 95 which is sent when a player changes chatbox settings.
 *
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public class ToggleChatMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 95: // Chat option changing
		byte status = (byte) in.readByte();
		if (status >= 0 && status <= 3) {
			player.setPublicChat(status);
		}
		status = (byte) in.readByte();
		if (status >= 0 && status <= 3) {
			player.setPrivateChat(status);
			player.getEncoder().updateOtherFriends(player.getPrivateChat());
		}
		status = (byte) in.readByte();
		if (status >= 0 && status <= 3) {
			player.setTradeCompete(status);
		}
		
	}

}
