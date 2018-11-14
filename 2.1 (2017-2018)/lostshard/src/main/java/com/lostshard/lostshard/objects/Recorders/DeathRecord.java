package com.lostshard.lostshard.Objects.Recorders;

import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.Type;

@Entity
public class DeathRecord extends Record {
	
	@Type(type = "uuid-char")
	private UUID player;
	
	@ElementCollection
	@LazyCollection(LazyCollectionOption.FALSE)
	@Type(type = "uuid-char")
	private Set<UUID> killers;
	
	/**
	 * @param player
	 */
	public DeathRecord(UUID player, Set<UUID> killers) {
		super();
		this.player = player;
		this.killers = killers;
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

	/**
	 * @return the killers
	 */
	public Set<UUID> getKillers() {
		return killers;
	}

	/**
	 * @param killers the killers to set
	 */
	public void setKillers(Set<UUID> killers) {
		this.killers = killers;
	}
	
	
	
}
