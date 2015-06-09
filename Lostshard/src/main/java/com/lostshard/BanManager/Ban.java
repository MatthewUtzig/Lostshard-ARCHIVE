package com.lostshard.BanManager;

import java.util.UUID;

public class Ban {
	
	private final int id;
	private final UUID player;
	private final UUID admin;
	private final String reason;
	private final long time;
	private final long duration;
	
	public Ban(int id, UUID player, UUID admin, String reason, long time, long duration) {
		this.id = id;
		this.player = player;
		this.admin = admin;
		this.reason = reason;
		this.time = time;
		this.duration = duration;
	}
	
	public Ban(UUID player, UUID admin, String reason, long time, long duration) {
		this.player = player;
		this.admin = admin;
		this.reason = reason;
		this.time = time;
		this.duration = duration;
		this.id = BanMapper.insertBan(this);
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