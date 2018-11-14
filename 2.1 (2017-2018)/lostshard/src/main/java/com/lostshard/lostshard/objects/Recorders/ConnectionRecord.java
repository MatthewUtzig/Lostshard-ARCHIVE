/**
 * 
 */
package com.lostshard.lostshard.Objects.Recorders;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;

/**
 * @author Jacob
 *
 */
@Entity
public class ConnectionRecord extends Record {

	@Type(type = "uuid-char")
	private UUID player;
	private String ip;
	private Date left_date;
	
	
	/**
	 * @param player
	 * @param ip
	 */
	public ConnectionRecord(UUID player, String ip) {
		super();
		this.player = player;
		this.ip = ip;
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
	 * @return the ip
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * @return the left_date
	 */
	public Date getLeft_date() {
		return left_date;
	}

	/**
	 * @param left_date the left_date to set
	 */
	public void setLeft_date(Date left_date) {
		this.left_date = left_date;
	}
}
