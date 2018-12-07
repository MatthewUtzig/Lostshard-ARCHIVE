package com.lostshard.lostshard.plot;

import org.apache.commons.lang.StringUtils;

public enum PlotUpgrade {
    TOWN("Town", 100000), DUNGEON("Dungeon", 20000), AUTOKICK("AutoKick", 10000), ARENA("Arena",
            10000), NEUTRALALIGNMENT("Neutral Alignment", 4000);

    public static PlotUpgrade getByName(String name) {
        for (final PlotUpgrade upgrade : values())
            if (StringUtils.startsWithIgnoreCase(upgrade.getDisplayName(), name))
                return upgrade;
        return null;
    }

    private int price;

    private String displayName;

    private PlotUpgrade(String displayName, int price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getPrice() {
        return this.price;
    }
}
