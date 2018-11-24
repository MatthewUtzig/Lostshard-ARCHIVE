package com.lostshard.lostshard.Objects.Player;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import javax.persistence.Entity;

@Entity
public class Reputation {

	private int murder = 100;
	private int criminal = 100;
	private int tradeing = 0;
	private int bounty = 0;

	public Reputation(int murder, int criminal, int tradeing, int bounty) {
		super();
		this.murder = murder;
		this.criminal = criminal;
		this.tradeing = tradeing;
		this.bounty = bounty;
	}

	public int getBounty() {
		return this.bounty;
	}

	public int getCriminal() {
		return this.criminal;
	}

	public int getMurder() {
		return this.murder;
	}

	public String getReputation() {
		final double total = this.getTotal() / 40d;
		final DecimalFormat format = new DecimalFormat("#0.0", new DecimalFormatSymbols(Locale.ENGLISH));
		format.setParseIntegerOnly(false);
		return format.format(total);
	}

	public int getTotal() {
		int total = 0;
		total += this.murder;
		total += this.criminal;
		total += this.tradeing;
		total += this.bounty;
		return total;
	}

	public int getTradeing() {
		return this.tradeing;
	}

	public void setBounty(int bounty) {
		this.bounty = bounty;
	}

	public void setCriminal(int criminal) {
		this.criminal = criminal;
	}

	public void setMurder(int murder) {
		this.murder = murder;
	}

	public void setTradeing(int tradeing) {
		this.tradeing = tradeing;
	}
}
