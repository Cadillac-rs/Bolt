package server.game.player.skill;

public final class SkillSet {

	/**
	 * Creates a new SkillSet
	 */
	public SkillSet() {
		// Set all skills to 1, and Hitpoints to 10.
		for (int i = 0; i < skills.length; i++) {
			skills[i] = new Skill();
			skills[i].setRealLevelAndExperience(i == 3 ? 10 : 1, true);
		}
	}
	
	/**
	 * All the skills the player has available.
	 */
	private final Skill[] skills = new Skill[24];

	
	public Skill get(int index) {
		return skills[index];
	}
	
	public void setSkills(Skill[] loaded) {
		for (int i = 0; i < skills.length; i++) {
			skills[i] = loaded[i];
		}
	}
	
	public Skill[] getSkills() {
		return skills;
	}
}
