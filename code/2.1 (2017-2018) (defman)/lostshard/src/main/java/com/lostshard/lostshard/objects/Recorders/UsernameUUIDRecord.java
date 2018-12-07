package com.lostshard.lostshard.Objects.Recorders;

import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

@Entity
public class UsernameUUIDRecord extends Record {

	@Type(type = "uuid-char")
	private UUID uuid;
	private String username;
	
	/**
	 * @param uuid
	 * @param username
	 */
	public UsernameUUIDRecord(UUID uuid, String username) {
		super();
		this.uuid = uuid;
		this.username = username;
	}

	/**
	 * @return the uuid
	 */
	public UUID getUuid() {
		return uuid;
	}
	
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
}
