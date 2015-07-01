package server.net.message.in.impl;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;
import server.util.Misc;

/**
 * Packet 185 sent when a player clicks a button in game.
 * 
 * @author Admin
 */
public class ButtonMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 185: // Button clicking.
		int button = Misc.hexToInt(in.readBytes(2)) ;
		
		switch (button) {
		case 9154:
			player.getEncoder().sendLogout();
			break;
		case 153:
			player.getMovementHandler().setRunToggled(true);
			break;
		case 152:
			player.getMovementHandler().setRunToggled(false);
			break;
		case 5451:
		case 5452:
			player.setBrightness((byte) 0);
			break;
		case 6273:
		case 6157:
			player.setBrightness((byte) 1);
			break;
		case 6275:
		case 6274:
			player.setBrightness((byte) 2);
			break;
		case 6277:
		case 6276:
			player.setBrightness((byte) 3);
			break;
		case 6279:
			player.setMouseButtons(true);
			break;
		case 6278:
			player.setMouseButtons(false);
			break;
		case 6280:
			player.setChatEffects(true);
			break;
		case 6281:
			player.setChatEffects(false);
			break;
		case 952:
			player.setSplitScreen(true);
			break;
		case 953:
			player.setSplitScreen(false);
			break;
		case 12591:
			player.setAcceptAid(true);
			break;
		case 12590:
			player.setAcceptAid(false);
			break;
		case 150:
			player.setRetaliate(true);
			break;
		case 151:
			player.setRetaliate(false);
			break;
		default:
			System.out.println("Unhandled button: " + button);
			break;
		}
		
	}

}
