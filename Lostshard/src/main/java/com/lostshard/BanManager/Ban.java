package com.lostshard.BanManager;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Ban {
	
	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;
	private UUID player;
	private UUID admin;
	private String reason;
	private long time;
	private long duration;
	
	public Ban(UUID player, UUID admin, String reason, long time, long duration) {
		this.player = player;
		this.admin = admin;
		this.reason = reason;
		this.time = time;
		this.duration = duration;
	}

	public int getId() {
		return id;
	}

	public UUID getPlayer() {
		return player;
	}

	public UUID getAdmin() {
		return admin;
	}

	public String getReason() {
		return reason;
	}

	public long getTime() {
		return time;
	}

	public long getDuration() {
		return duration;
	}
}