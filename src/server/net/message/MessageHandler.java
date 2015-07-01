package server.net.message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import server.game.player.Player;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.message.in.impl.AddFriendMessage;
import server.net.message.in.impl.ButtonMessage;
import server.net.message.in.impl.ChatMessage;
import server.net.message.in.impl.CommandMessage;
import server.net.message.in.impl.EquipItemMessage;
import server.net.message.in.impl.FriendChatMessage;
import server.net.message.in.impl.MovementMessage;
import server.net.message.in.impl.RemoveFriendMessage;
import server.net.message.in.impl.RemoveIgnoreMessage;
import server.net.message.in.impl.RemoveItemMessage;
import server.net.message.in.impl.ToggleChatMessage;
import server.net.util.StreamBuffer.InBuffer;

public class MessageHandler {

	private static final Map<Integer, MessageDecoder> decoders = new HashMap<>();
	
	public static void decode(Player player, InBuffer in, ReceivedPacket packet) {
		
		Optional<MessageDecoder> decoder = Optional.ofNullable(decoders.get(packet.getOpcode()));
		
		decoder.ifPresent(d ->  { 
			d.decode(player, in, packet);
			return;
		});
		
		switch (packet.getOpcode()) {
		
		case 0:
		case 3:
		case 77:
		case 121: // loading screen packet. Region change
		case 164: // don't know
		case 177: // rotate camera
		case 202: // unknown
		case 226: // unknown
		case 241: // clicking screen packet
			break;
		
		default:
			System.out.println(player.toString() + " unhandled packet received " + packet.getOpcode() + " - " + packet.getSize());
			break;
		}
	}
	
	static {
		decoders.put(4, new ChatMessage());
		decoders.put(41, new EquipItemMessage());
		decoders.put(74, new RemoveIgnoreMessage());
		decoders.put(95, new ToggleChatMessage());
		decoders.put(98, new MovementMessage());
		decoders.put(103, new CommandMessage());
		decoders.put(126, new FriendChatMessage());
		decoders.put(133, new RemoveIgnoreMessage());
		decoders.put(145, new RemoveItemMessage());
		decoders.put(164, new MovementMessage());
		decoders.put(185, new ButtonMessage());
		decoders.put(188, new AddFriendMessage());
		decoders.put(215, new RemoveFriendMessage());
		decoders.put(248, new MovementMessage());
	}
	
}
