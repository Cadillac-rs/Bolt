package server.game.world;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Tasks {

	private final List<Task> list = new LinkedList<>();

	public void submit(Task task) {
		list.add(task);
	}
	
	public Tasks() {
	}
	
	public void tick() {
		Iterator<Task> it = list.iterator();
		
		// Loops the entity's tasks.
		while (it.hasNext()) {
			
			// The task we are manipulating.
			Task task = it.next();
			
			// Decrement the tick of the task.
			if (task.decrement() == 0) {
				task.execute();
				
				// If the task is over we finish and remove it.
				if (!task.isRepeat()) {
					task.onFinish();
					it.remove();
				} else {
					task.setTime(task.nextDelay());
				}
			}
		}
		
	}
	
	public List<Task> getList() {
		return list;
	}
	
}
