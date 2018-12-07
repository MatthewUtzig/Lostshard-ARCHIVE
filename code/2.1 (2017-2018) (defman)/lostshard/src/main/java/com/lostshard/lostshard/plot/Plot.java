package com.lostshard.lostshard.plot;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.lostshard.lostshard.Objects.Plot.PlotCapturePoint;
import org.bukkit.Location;
import com.lostshard.lostshard.NPC.NPC;
import com.lostshard.lostshard.Objects.PlayerSet;
import com.lostshard.lostshard.Objects.Wallet;

public class Plot {

    private String name;
    private UUID owner;
    private final Location location;
    private int size;
    private PlayerSet friends;
    private PlayerSet coowners;
    private Set<PlotUpgrade> upgrades;
    private Set<PlotFlag> flags;
    private Set<NPC> npcs;

    private PlotCapturePoint capturepointData = null;
    private final Wallet wallet = new Wallet();

    public Plot(String name, UUID owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
        this.size = 10;
        this.friends = new PlayerSet();
        this.coowners = new PlayerSet();
        this.upgrades = new HashSet<>();
        this.flags = new HashSet<>(2);

        this.npcs = new HashSet<>();

        this.flags.add(PlotFlag.PROTECTION);
        this.flags.add(PlotFlag.PRIVATE);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public Location getLocation() {
        return location;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public PlayerSet getFriends() {
        return friends;
    }

    public void setFriends(PlayerSet friends) {
        this.friends = friends;
    }

    public PlayerSet getCoowners() {
        return coowners;
    }

    public void setCoowners(PlayerSet coowners) {
        this.coowners = coowners;
    }

    public Set<PlotUpgrade> getUpgrades() {
        return upgrades;
    }

    public void setUpgrades(Set<PlotUpgrade> upgrades) {
        this.upgrades = upgrades;
    }

    public Set<PlotFlag> getFlags() {
        return flags;
    }

    public void setFlags(Set<PlotFlag> flags) {
        this.flags = flags;
    }

    public Set<NPC> getNpcs() {
        return npcs;
    }

    public void setNpcs(Set<NPC> npcs) {
        this.npcs = npcs;
    }

    public PlotCapturePoint getCapturepointData() {
        return capturepointData;
    }

    public void setCapturepointData(PlotCapturePoint capturepointData) {
        this.capturepointData = capturepointData;
    }

    public Wallet getWallet() {
        return wallet;
    }
}
