package server.game.player.skill;

public class SkillSet {

	/**
	 * Creates a new SkillSet
	 */
	public SkillSet() {
		// Set all skills to 1, and Hitpoints to 10.
		for (int i = 0; i < skills.length; i++) {
			if (i == 3) { // Hitpoints.
				skills[i].setLevel(10, true);
				skills[i].setExperience(1154);
			} else {
				skills[i].setLevel(1, true);
			}
		}
	}
	
	/**
	 * All the skills the player has available.
	 */
	private final Skill[] skills = new Skill[24];

	public Skill[] getSkills() {
		return skills;
	}
	
	public void setSkills(Skill[] loaded) {
		for (int i = 0; i < skills.length; i++) {
			skills[i] = loaded[i];
		}
	}
}
