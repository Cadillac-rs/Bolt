package server.game.player.skill.task;

import java.util.LinkedList;
import java.util.List;

import server.game.player.Player;

public class SkillTask {
	
	private static final List<Player> observers = new LinkedList<>();

	public static void add(Player player) {
		observers.add(player);
	}
	
	public static void remove(Player player) {
		observers.remove(player);
	}
	
	public void notifyObservers() {
		for (Player o : observers) {
			// Observer logic.
		}
	}
	
	public static List<Player> getObservers() {
		return observers;
	}

}
