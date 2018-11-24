package com.lostshard.Alts;

import java.net.Inet4Address;
import java.util.Date;
import java.util.UUID;

public class PlayerConnection {

	private final Inet4Address ip;
	private final UUID player;
	private final Date join;
	private final Date quit;

	public PlayerConnection(Inet4Address ip, UUID player) {
		this(ip, player, new Date(), null);
	}

	public PlayerConnection(Inet4Address ip, UUID player, Date join, Date quit) {
		this.ip = ip;
		this.player = player;
		this.join = join;
		this.quit = quit;
	}

	public Inet4Address getIp() {
		return this.ip;
	}

	public Date getJoin() {
		return this.join;
	}

	public UUID getPlayer() {
		return this.player;
	}

	public Date getQuit() {
		return this.quit;
	}
}
