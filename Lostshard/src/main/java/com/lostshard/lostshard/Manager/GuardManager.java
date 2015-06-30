package com.lostshard.lostshard.Manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Tasks.GuardTask;

public class GuardManager {

	private static GuardManager manager = new GuardManager();
	
	private final List<GuardTask> tasks = new ArrayList<GuardTask>();
	
	private GuardManager() {}
	
	public static GuardManager getManager() {
		return manager;
	}

	public void add(Player c, NPC guard, int wait) {
		tasks.add(new GuardTask(guard, c.getUniqueId(), wait));
	}

	public List<GuardTask> getTasks() {
		return tasks;
	}
	
	public void tick() {
		for(GuardTask gt : tasks)
			gt.tick();
		tasks.removeIf(gt -> gt.remove());
	}
}
