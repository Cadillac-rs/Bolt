package server.game.player.update;

import java.util.concurrent.ForkJoinPool;

public class ForkJoinPlayerUpdater {

	private final ForkJoinPool updater = new ForkJoinPool(
			Runtime.getRuntime().availableProcessors()
	);
	
	public void execute() {
		updater.execute(new PlayerUpdateAction());
	}
	
}
