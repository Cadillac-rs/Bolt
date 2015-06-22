package server.game.entity;

import server.game.Position;

/**
 * An entity, the least specific type of ingame interactable entity.
 * 
 * @author Cadillac
 */
public abstract class Entity {

	/**
	 * The slot of this entity.
	 */
	private int slot;
	
	/**
	 * The position this entity is located.
	 */
	private Position position = new Position(3222, 3222);

	/**
	 * A temporary instance of the most recent position for changing regions.
	 */
	private Position currentRegion = new Position(0, 0, 0);
	
	/**
	 * The movement processor.
	 */
	private final MovementHandler movementHandler = new MovementHandler(this);
	
	/**
	 * A flag determining if this entity needs to be updated.
	 */
	private boolean updateRequired;
	
	/**
	 * The direction this entity is facing.
	 */
	private int primaryDirection = -1;
	
	/**
	 * The secondary direction this entity is facing.
	 */
	private int secondaryDirection = -1;
	
	/**
	 * The 600ms pulse.
	 */
	public abstract void process();
	
	/**
	 * Reset the entity.
	 */
	public abstract void reset();
	
	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		position.setAs(position);
		// XXX: Change region
	}

	public MovementHandler getMovementHandler() {
		return movementHandler;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
	}

	public boolean isUpdateRequired() {
		return updateRequired;
	}

	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}
	
	public void setPrimaryDirection(int primaryDirection) {
		this.primaryDirection = primaryDirection;
	}

	public int getSecondaryDirection() {
		return secondaryDirection;
	}

	public void setSecondaryDirection(int secondaryDirection) {
		this.secondaryDirection = secondaryDirection;
	}

	public Position getCurrentRegion() {
		return currentRegion;
	}

	public void setCurrentRegion(Position newRegion) {
		currentRegion.setAs(newRegion);
	}
	
	/**
	 * Sets the player slot.
	 * 
	 * @param slot
	 *            the slot
	 */
	public void setSlot(int slot) {
		this.slot = slot;
	}

	/**
	 * Gets the player slot.
	 * 
	 * @return the slot
	 */
	public int getSlot() {
		return slot;
	}
	
}
