package server.game.world;

import server.game.Position;

/**
 * The least specific type of ingame interactable entity. It is, however,
 * positionable on the game map.
 * 
 * @author Cadillac
 */
public abstract class Positionable {

	/**
	 * The slot of this entity.
	 */
	private int slot;
	
	/**
	 * The position this entity is located.
	 */
	private Position position = new Position(3222, 3222);

	/**
	 * The object that contains {@link Task}s this entity needs to execute.
	 */
	private final Tasks tasks = new Tasks();
	
	public final int getSlot() {
		return slot;
	}
	
	public final void setSlot(int slot) {
		this.slot = slot;
	}
	
	public Position getPosition() {
		return position;
	}

	public Tasks getTasks() {
		return tasks;
	}
	
	public void setPosition(Position position) {
		position.setAs(position);
		// XXX: Change region
	}
	
	/**
	 * The 600ms pulse.
	 */
	public abstract void process();
	
	/**
	 * 
	 */
	public abstract void destroy();
	
}
