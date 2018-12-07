package com.lostshard.BanManager;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;

@Entity
public class Ban {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	private final UUID player;
	private final UUID admin;
	private final String reason;
	private final long time;
	private final long duration;

	public Ban(UUID player, UUID admin, String reason, long time, long duration) {
		this.player = player;
		this.admin = admin;
		this.reason = reason;
		this.time = time;
		this.duration = duration;
	}

	public UUID getAdmin() {
		return this.admin;
	}

	public long getDuration() {
		return this.duration;
	}

	public int getId() {
		return this.id;
	}

	public UUID getPlayer() {
		return this.player;
	}

	public String getReason() {
		return this.reason;
	}

	public long getTime() {
		return this.time;
	}
}