package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import com.lostshard.lostshard.Tasks.DelayedTask;

public class TaskManager {

	private static TaskManager manager = new TaskManager();

	public static TaskManager getManager() {
		return manager;
	}

	private final List<DelayedTask> tasks = new ArrayList<DelayedTask>();

	private TaskManager() {
	}

	public void add(DelayedTask task) {
		this.tasks.add(task);
	}

	public List<DelayedTask> getTasks() {
		return this.tasks;
	}

	public void tick() {
		for (final DelayedTask dt : this.tasks)
			dt.tick();
		this.tasks.removeIf(dt -> dt.remove());
	}
}
