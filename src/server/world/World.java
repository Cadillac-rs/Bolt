package server.world;

import server.core.GameService;

public class World {

	private static final GameService gameService = new GameService();
	

	public static GameService getService() {
		return gameService;
	}
	
}
