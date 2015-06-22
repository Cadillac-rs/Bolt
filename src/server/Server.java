package server;
/*
 * This file is part of RuneSource.
 *
 * RuneSource is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RuneSource is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with RuneSource.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import server.core.GameProcessor;
import server.core.GameService;
import server.game.npc.Npc;
import server.game.player.PlayerHandler;
import server.net.PipelineFactory;
import server.world.World;

/**
 * The main core of RuneSource.
 * 
 * @author blakeman8192
 */
public class Server {

	private static Server singleton;
	
	private static final String host = "localhost";
	private static final int port = 43594;
	private static final byte world = 1;

	private static InetSocketAddress address;


	/**
	 * The main method.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		run();
		
	}

	public static void run() {
		try {
			address = new InetSocketAddress(host, port);
			System.out.println("Starting RuneSource on " + address + "...");

			startup();
			System.out.println("Online!");
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Starts the server up.
	 * 
	 * @throws IOException
	 */
	private static void startup() throws IOException {
		// Initialize netty and begin listening for new clients
		ServerBootstrap serverBootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory());
		serverBootstrap.bind(address);

		PlayerHandler.register(new Npc(1));
		
		new GameProcessor().init();
		
		System.out.println("World: " + world);
	}

	/**
	 * Sets the server singleton object.
	 * 
	 * @param singleton
	 *            the singleton
	 */
	public static void setSingleton(Server singleton) {
		if (Server.singleton != null) {
			throw new IllegalStateException("Singleton already set!");
		}
		Server.singleton = singleton;
	}

	/**
	 * Gets the server singleton object.
	 * 
	 * @return the singleton
	 */
	public static Server getSingleton() {
		return singleton;
	}

	public byte getWorld() {
		return world;
	}

}
