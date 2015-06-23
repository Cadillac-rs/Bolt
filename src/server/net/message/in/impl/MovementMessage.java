package server.net.message.in.impl;

import server.game.Position;
import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer;
import server.net.util.StreamBuffer.InBuffer;

public class MovementMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
//	case 248: // Movement.
	//case 164: // ^
	//case 98: // ^
		int length = packet.getSize();
		if (packet.getOpcode() == 248) {
			length -= 14;
		}
		int steps = (length - 5) / 2;
		int[][] path = new int[steps][2];
		int firstStepX = in.readShort(StreamBuffer.ValueType.A, StreamBuffer.ByteOrder.LITTLE);
		for (int i = 0; i < steps; i++) {
			path[i][0] = in.readByte();
			path[i][1] = in.readByte();
		}
		int firstStepY = in.readShort(StreamBuffer.ByteOrder.LITTLE);

		player.getMovementHandler().reset();
		player.getMovementHandler().setRunPath(in.readByte(StreamBuffer.ValueType.C) == 1);
		player.getMovementHandler().addToPath(new Position(firstStepX, firstStepY));
		for (int i = 0; i < steps; i++) {
			path[i][0] += firstStepX;
			path[i][1] += firstStepY;
			player.getMovementHandler().addToPath(new Position(path[i][0], path[i][1]));
		}
		player.getMovementHandler().finish();
		
		
	}

}
