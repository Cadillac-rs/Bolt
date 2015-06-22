package server.game.player.skill;

public final class ExperienceTable {

	/**
	 * A table indexed by level from (0-120) holding the amount of experience
	 * it takes to be at each level according to the index.
	 */
	private static final int[] TABLE = new int[121];
	
	/**
	 * Gets the amount of experience for a given level.
	 * See {@link ExperienceTable#TABLE}.
	 * 
	 * @param level
	 * 			The level you want to get the minimum amount of experience for.
	 * @return
	 * 			The minimum amount of experience required for this level.
	 */
	public static int get(int level) {
		return TABLE[level];
	}
	
	/**
     * Gets the amount of experience for a level.
     * 
     * @param level
     * 			The level to find experience for.
     * @return
     * 			The amount of experience
     */
    private static int getExperienceForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double)lvl + 300.0 * Math.pow(2.0, (double)lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int)Math.floor(points / 4);
		}
		
		return 0;
	}
    
    static {
    	
    	// Set the amount of experience for each table index.
    	for (int index = 0; index < TABLE.length; index++) {
    		TABLE[index] = getExperienceForLevel(index);
    	}
    	
    }
}
