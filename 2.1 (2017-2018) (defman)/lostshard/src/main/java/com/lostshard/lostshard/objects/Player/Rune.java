package com.lostshard.lostshard.Objects.Player;

import org.bukkit.Location;

public class Rune {

    private String label;
    private Location location;

    public Rune(String label, Location location) {
        this.label = label;
        this.location = location;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
