package com.lostshard.BanManagement.Models;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;

@Entity
public class Ban {
	private Date issued;
	private long duration;
	
	private Reason reason;
	private String other;
	
	private UUID issuer;
	
	private UUID player;
	
	private boolean finalized;
	
	
	/**
	 * @param duration
	 * @param reason
	 * @param description
	 * @param issuer
	 * @param player
	 */
	public Ban(long duration, Reason reason, String other, UUID issuer, UUID player) {
		super();
		this.issued = new Date();
		this.duration = duration;
		this.reason = reason;
		this.other = other;
		this.issuer = issuer;
		this.player = player;
		this.finalized = false;
	}

	/**
	 * @return the issued
	 */
	public Date getIssued() {
		return issued;
	}

	/**
	 * @param issued the issued to set
	 */
	public void setIssued(Date issued) {
		this.issued = issued;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the reason
	 */
	public Reason getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(Reason reason) {
		this.reason = reason;
	}

	/**
	 * @return the description
	 */
	public String getOther() {
		return other;
	}

	/**
	 * @param description the description to set
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return the issuer
	 */
	public UUID getIssuer() {
		return issuer;
	}

	/**
	 * @param issuer the issuer to set
	 */
	public void setIssuer(UUID issuer) {
		this.issuer = issuer;
	}

	/**
	 * @return the player
	 */
	public UUID getPlayer() {
		return player;
	}

	/**
	 * @param player the player to set
	 */
	public void setPlayer(UUID player) {
		this.player = player;
	}

	/**
	 * @return the finalized
	 */
	public boolean isFinalized() {
		return finalized;
	}

	/**
	 * @param finalized the finalized to set
	 */
	public void setFinalized(boolean finalized) {
		this.finalized = finalized;
	}
}
