package com.lostshard.lostshard.Objects.Plot;

import org.apache.commons.lang.StringUtils;

public enum PlotUpgrade {

	TOWN("Town", 100000),
	DUNGEON("Dungeon", 20000),
	AUTOKICK("AutoKick", 10000),
	ARENA("Arena", 10000),
	NEUTRALALIGNMENT("Neutral Alignment", 4000);
	
	private int price;
	private String name;
	
	private PlotUpgrade(String name, int price) {
		this.setName(name);
		this.setPrice(price);
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static PlotUpgrade getByName(String name) {
		for(PlotUpgrade upgrade : values())
			if(StringUtils.startsWithIgnoreCase(upgrade.getName(), name))
				return upgrade;
		return null;
	}

}
