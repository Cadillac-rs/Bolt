package server.game.player.skill;

import java.util.Optional;

import server.game.player.Player;
import server.game.player.skill.task.SkillTask;
import server.game.player.skill.task.SkillLogic;
import server.util.MutableNumber;

/**
 * Manages {@link SkillLogic} children and acts as the <code>Observer</code> to {@link SkillTask}.
 * 
 * @Author Cadillac 
 */
public class SkillManager {

	/**
	 * The player's skill tick. 
	 */
	private final MutableNumber skillTick = new MutableNumber();

	private Optional<SkillLogic> activeSkill;
	
	public final void observe(Player player) {
		
		if (!activeSkill.isPresent()) {
			SkillTask.remove(player);
			return;
		}
		
		SkillLogic skill = activeSkill.get();
		
		if (skillTick.get() == 0) {
			skill.onSuccess(player);
		}
	}
	
	public void start(Player player, SkillLogic skill) {
		if (activeSkill.isPresent()) {
			finish(player);
		}
		
		activeSkill = Optional.ofNullable(skill);
		skillTick.set(activeSkill.get().successDelay(player));
		SkillTask.add(player);
	}
	
	public final void finish(Player player) {
		SkillTask.remove(player);
		skillTick.set(0);
		activeSkill = Optional.empty();
	}
	
}
