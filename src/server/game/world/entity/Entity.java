package server.game.world.entity;

import server.game.Position;
import server.game.world.MovementHandler;
import server.game.world.Positionable;

public abstract class Entity extends Positionable {
	
	/**
	 * The movement processor.
	 */
	private final MovementHandler movementHandler = new MovementHandler(this);
	
	/**
	 * A temporary instance of the most recent position for changing regions.
	 */
	private Position previousPosition = new Position(0, 0, 0);
	
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

	public Position getPreviousPosition() {
		return previousPosition;
	}

	public void setPreviousPosition(Position previousPosition) {
		this.previousPosition = previousPosition;
	}

	public boolean isUpdateRequired() {
		return updateRequired;
	}

	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public int getPrimaryDirection() {
		return primaryDirection;
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

	public MovementHandler getMovementHandler() {
		return movementHandler;
	}
	
	/**
	 * Reset the entity.
	 */
	public abstract void reset();
}
