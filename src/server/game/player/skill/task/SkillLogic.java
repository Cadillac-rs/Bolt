package server.game.player.skill.task;

import server.game.player.Player;

public abstract class SkillLogic {
	
	public abstract void onSuccess(Player player);
	
	public abstract int successDelay(Player player);
	
	
}
