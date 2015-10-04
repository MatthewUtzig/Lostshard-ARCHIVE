package com.lostshard.Lostshard.Manager;

import java.util.ArrayList;
import java.util.List;


import com.lostshard.Lostshard.Tasks.DelayedTask;

public class TaskManager {

	private static TaskManager manager = new TaskManager();

	private final List<DelayedTask> tasks = new ArrayList<DelayedTask>();
	
	private TaskManager() {}
	
	public static TaskManager getManager() {
		return manager;
	}

	public List<DelayedTask> getTasks() {
		return tasks;
	}
	
	public void add(DelayedTask task) {
		tasks.add(task);
	}
	
	public void tick() {
		for(DelayedTask dt : tasks)
			dt.tick();
		tasks.removeIf(dt -> dt.remove());
	}
}
