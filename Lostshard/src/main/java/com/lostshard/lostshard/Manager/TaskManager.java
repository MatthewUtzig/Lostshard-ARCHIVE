package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;

import com.lostshard.lostshard.Tasks.DelayedTask;

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
		if(tasks.size()>0)
			Bukkit.broadcastMessage("Tick: "+tasks.size());
		for(DelayedTask dt : tasks)
			dt.tick();
		tasks.removeIf(dt -> dt.remove());
	}
}
