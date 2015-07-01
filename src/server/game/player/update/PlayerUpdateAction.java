package server.game.player.update;

import java.util.concurrent.RecursiveAction;

import server.game.player.PlayerHandler;


public class PlayerUpdateAction extends RecursiveAction {

	private static final long serialVersionUID = 1L;

    public PlayerUpdateAction() {
	
    }

    @Override
    protected void compute() {
    	try {
			PlayerHandler.process();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}
