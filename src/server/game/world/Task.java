package server.game.world;

public abstract class Task {
	
	/**
	 * The amount of time before the {@link #execute} method is called.
	 */
	private int time = 0;
	
	/**
	 * A cached value of the original timer.
	 */
	private final int initialTime;
	
	/**
	 * A flag determining whether we should continue this {@link Work}.
	 */
	private boolean repeat;
	
	/**
	 * The execution to be handled.
	 * 
	 * @param entity
	 */
	public abstract void execute();
	
	public void onFinish() {
		// empty method, as it won't be used for every implementation.
	}
	
	public Task(int time, boolean repeat) {
		this.time = time;
		this.initialTime = time;
		this.repeat = repeat;
	}

	public void setRepeat(boolean flag) {
		repeat = flag;
	}
	
	public boolean isRepeat() {
		return repeat;
	}

	public final int getTime() {
		return time;
	}

	public int nextDelay() {
		return initialTime;
	}
	
	public final void setTime(int time) {
		this.time = time;
	}

	public final int decrement() {
		return time--;
	}
	
	public final void forceFinish(Positionable positionable) {
		positionable.getTasks().getList().remove(this);
	}
	
}
