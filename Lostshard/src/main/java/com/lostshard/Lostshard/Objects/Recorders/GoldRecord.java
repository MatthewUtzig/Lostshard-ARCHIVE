package com.lostshard.Lostshard.Objects.Recorders;

import java.util.UUID;

public class GoldRecord extends Record {

	private int amount;
	
	private String cause;
	
	private UUID from;
	private UUID to;
	
	/**
	 * @param amount
	 * @param from
	 * @param to
	 */
	public GoldRecord(int amount, String cause, UUID from, UUID to) {
		super();
		this.amount = amount;
		this.from = from;
		this.to = to;
	}
	
	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}
	
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	/**
	 * @return the cause
	 */
	public String getCause() {
		return cause;
	}
	
	/**
	 * @param cause the cause to set
	 */
	public void setCause(String cause) {
		this.cause = cause;
	}
	
	/**
	 * @return the from
	 */
	public UUID getFrom() {
		return from;
	}
	/**
	 * @param from the from to set
	 */
	public void setFrom(UUID from) {
		this.from = from;
	}
	
	/**
	 * @return the to
	 */
	public UUID getTo() {
		return to;
	}
	
	/**
	 * @param to the to to set
	 */
	public void setTo(UUID to) {
		this.to = to;
	}
}
