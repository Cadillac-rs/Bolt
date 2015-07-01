package server.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import server.world.World;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class GameProcessor {

	/**
	 * The thread which will handle the 600 ms game tick.
	 */
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(new ThreadFactoryBuilder().setNameFormat("GameProcessor").build());
	
	public void init() {
		executor.scheduleAtFixedRate(World.getService(), 0, 600, TimeUnit.MILLISECONDS);
	}
	
}
