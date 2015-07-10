package com.lostshard.Lostshard.Objects.Plot;

import org.apache.commons.lang.StringUtils;

public enum PlotUpgrade {

	TOWN("Town", 100000), DUNGEON("Dungeon", 20000), AUTOKICK("AutoKick", 10000), ARENA(
			"Arena", 10000), NEUTRALALIGNMENT("Neutral Alignment", 4000);

	public static PlotUpgrade getByName(String name) {
		for (final PlotUpgrade upgrade : values())
			if (StringUtils.startsWithIgnoreCase(upgrade.getName(), name))
				return upgrade;
		return null;
	}

	private int price;

	private String name;

	private PlotUpgrade(String name, int price) {
		this.setName(name);
		this.setPrice(price);
	}

	public String getName() {
		return this.name;
	}

	public int getPrice() {
		return this.price;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

}
