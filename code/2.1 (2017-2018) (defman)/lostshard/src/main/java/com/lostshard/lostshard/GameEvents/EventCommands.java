package com.lostshard.lostshard.GameEvents;

import java.util.Date;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Text;
import com.sk89q.intake.parametric.annotation.Validate;

public class EventCommands {

	private static EventManager em = EventManager.getManager();
	
	public void create(@Sender Player player, String event, int size, int duration, @Optional @Text @Validate(regex="\\w{2, 20}") String title) {
		final long start = new Date().getTime();
		final long stop = start + duration * 1000;
		
		em.createEvent(event, player, size, title, start, stop);
	}
	
	public void start() {
		
	}
	
	public void stop() {
		
	}
	
	public void pause() {
		
	}
}
