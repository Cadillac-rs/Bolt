package server.game.player;

/**
 * Represents the rights and authority of a {@link Player}
 * 
 * 
 * @author Cadillac <http://github.com/cadillac-rs>
 */
public enum Authority {

	PLAYER(0,0),
	DONATOR(3,0),
	MODERATOR(1,1),
	ADMINISTRATOR(2,2),
	OWNER(2,3);

	/**
	 * The index of the crown in the cache this player should display.
	 */
	private final int crown;
	
	/**
	 * The numerical index of this player's privilege. 
	 */
	private final int privilege;

	private Authority(int crown, int privilege) {
		this.crown = crown;
		this.privilege = privilege;
	}
	
	public int getCrown() {
		return crown;
	}

	public int getPrivilege() {
		return privilege;
	}
}
