package server.net.message.in.impl;

import server.Server;
import server.game.player.Player;
import server.game.player.PlayerHandler;
import server.net.ReceivedPacket;
import server.net.message.in.MessageDecoder;
import server.net.util.StreamBuffer.InBuffer;
import server.util.Misc;

/**
 * Packet 188 which is sent when a player adds a new friend.
 *
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public class AddFriendMessage implements MessageDecoder {

	@Override
	public void decode(Player player, InBuffer in, ReceivedPacket packet) {
	//case 188: // Add friend
		long friend = in.readLong();
		if(player.getFriends().size() >= 200) {
			player.getEncoder().sendMessage("Friends list is full.");
			return;
		}
		if(player.getFriends().contains(friend)) {
			player.getEncoder().sendMessage("That player is already on your friends list.");
			return;
		}
		player.getFriends().add(friend);
		Player plr = PlayerHandler.getPlayerByName(Misc.longToName(friend));
		byte world = 0;
		if(plr != null) {
			if(plr.getPrivateChat() == 0) {
				world = Server.getSingleton().getWorld();
			} else if(plr.getPrivateChat() == 1) {
				if(plr.hasFriend(Misc.nameToLong(player.getUsername()))) {
					world = Server.getSingleton().getWorld();
				}
			}
			if(player.getPrivateChat() == 1 && plr.hasFriend(Misc.nameToLong(player.getUsername()))) {
				plr.getEncoder().sendFriendUpdate(Misc.nameToLong(player.getUsername()), Server.getSingleton().getWorld());
			}
		}
		player.getEncoder().sendFriendUpdate(friend, world);
		
	}

	
	
}
