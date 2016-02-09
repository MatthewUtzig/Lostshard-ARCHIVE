package com.lostshard.Lostshard.Objects;

import java.util.Date;

import javax.annotation.concurrent.Immutable;
import javax.persistence.Embeddable;

@Embeddable
@Immutable
public class GoldCoinTransaction extends Number implements Comparable<GoldCoinTransaction> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final Date transactionDate;
	
	private final int fromValue;
	private final int value;
	private final Wallet from;
	private final Wallet to;
	
	private final String note;
	
	public GoldCoinTransaction(Wallet from, Wallet to, int value, String note) {
		this.transactionDate = new Date();
		this.from = from;
		this.to = to;
		this.fromValue = to.intValue();
		this.value = to.intValue()+value;
		this.note = note;
	}

	@Override
	public double doubleValue() {
		return Math.floor(value);
	}

	@Override
	public float floatValue() {
		return (float) Math.floor(value);
	}

	@Override
	public int intValue() {
		return value;
	}

	@Override
	public long longValue() {
		return value;
	}

	@Override
	public int compareTo(GoldCoinTransaction gc) {
		return value - gc.value;
	}

	public boolean isLess(int amount) {
		return value < amount;
	}
	
	/**
	 * @return the transactionDate
	 */
	public Date getTransactionDate() {
		return transactionDate;
	}
	
	/**
	 * @return the fromValue
	 */
	public int getFromValue() {
		return fromValue;
	}

	/**
	 * @return the toValue
	 */
	public int getValue() {
		return value;
	}

	/**
	 * @return the from
	 */
	public Wallet getFrom() {
		return from;
	}
	
	/**
	 * @return the to
	 */
	public Wallet getTo() {
		return to;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
}
