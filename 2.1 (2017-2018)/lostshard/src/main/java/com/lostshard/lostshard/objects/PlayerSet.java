package com.lostshard.lostshard.Objects;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.ForwardingSet;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PlayerSet extends ForwardingSet<UUID> {

    private final Set<UUID> players;

    public PlayerSet(Set<UUID> players) {
        this.players = players;
    }

    public PlayerSet() {
        this(new HashSet<>());
    }

    @Override
    protected Set<UUID> delegate() {
        return this.players;
    }

    public Set<OfflinePlayer> offlinePlayers() {
        return players.stream()
                .map(Bukkit::getOfflinePlayer)
                .collect(Collectors.toSet());
    }

    public Set<Player> players() {
        return this.players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<String> usernames() {
        return players.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .map(Player::getName)
                .collect(Collectors.toSet());
    }
}
