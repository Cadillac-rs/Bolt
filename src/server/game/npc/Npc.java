package server.game.npc;

import server.game.world.entity.Entity;

/**
 * A non-player-character. Extends Player so that we can share the many
 * attributes.
 * 
 * @author blakeman8192
 */
public class Npc extends Entity {

	/** The NPC ID. */
	private int npcId;

	/** Whether or not the NPC is visible. */
	private boolean isVisible = true;

	/**
	 * Creates a new Npc.
	 * 
	 * @param npcId
	 *            the NPC ID
	 */
	public Npc(int npcId) {
		setNpcId(npcId);
	}

	@Override
	public void process() {
		// NPC-specific processing.
		//getMovementHandler().process();
	}

	@Override
	public void reset() {
		//super.reset();
		// TODO: Any other NPC resetting that isn't in Player.
	}

	/**
	 * Sets the NPC ID.
	 * 
	 * @param npcId
	 *            the npcId
	 */
	public void setNpcId(int npcId) {
		this.npcId = npcId;
	}

	/**
	 * Gets the NPC ID.
	 * 
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public boolean isVisible() {
		return isVisible;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
