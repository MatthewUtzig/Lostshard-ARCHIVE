package com.lostshard.Lostshard.Objects.Recorders;

import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class DeathRecord extends Record {
	
	@Type(type = "uuid-char")
	private UUID player;
	
	/**
	 * @param player
	 */
	public DeathRecord(UUID player) {
		super();
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public UUID getPlayer() {
		return this.player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(UUID player) {
		this.player = player;
	}
	
	
	
}
