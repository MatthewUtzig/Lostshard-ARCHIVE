package com.lostshard.banManager.Objects;

import java.util.Date;
import java.util.UUID;

public class Ban {

	private UUID banned;
	private String banned_ip;
	private long expires = 0;
	private String reason;
	private long time;
	private UUID by;
	
	public Ban(UUID banned, String banned_ip, long expires, String reason,
			long time, UUID by) {
		super();
		this.banned = banned;
		this.banned_ip = banned_ip;
		this.expires = expires;
		this.reason = reason;
		this.time = time;
		this.by = by;
	}
	
	public Ban(UUID banned, String banned_ip, long expires, String reason, UUID by) {
		super();
		this.banned = banned;
		this.banned_ip = banned_ip;
		this.expires = expires;
		this.reason = reason;
		this.time = new Date().getTime();
		this.by = by;
	}

	public UUID getBanned() {
		return banned;
	}

	public void setBanned(UUID banned) {
		this.banned = banned;
	}

	public String getBanned_ip() {
		return banned_ip;
	}

	public void setBanned_ip(String banned_ip) {
		this.banned_ip = banned_ip;
	}

	public long getExpires() {
		return expires;
	}

	public void setExpires(long expires) {
		this.expires = expires;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public UUID getBy() {
		return by;
	}

	public void setBy(UUID by) {
		this.by = by;
	}
	
}
