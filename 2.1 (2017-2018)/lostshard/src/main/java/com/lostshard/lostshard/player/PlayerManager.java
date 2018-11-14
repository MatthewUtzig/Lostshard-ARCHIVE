package com.lostshard.lostshard.player;

import com.lostshard.lostshard.main.Manager;
import com.lostshard.lostshard.main.ServiceStartException;
import com.mongodb.async.client.MongoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PlayerManager implements Manager<PlayerManager>, Listener {

    private final Map<UUID, LSPlayer> players;
    private final PlayerRepository playerRepository;

    public PlayerManager(MongoDatabase mongoDatabase) {
        this.playerRepository = new PlayerRepository(mongoDatabase.getCollection("Players", LSPlayer.class));
        this.players = new HashMap<>();
    }

    @Override
    public void enable() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        playerRepository.load(() -> {
            latch.countDown();
        }, Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).toArray(i -> new UUID[i]));
        if(!latch.await(1000, TimeUnit.MILLISECONDS));
            throw new ServiceStartException("[PlayerManger] Timed out while fetching player data.");
    }

    @Override
    public void disable() {
        return true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void loadPlayer(AsyncPlayerPreLoginEvent event) {
        CountDownLatch latch = new CountDownLatch(1);
        playerRepository.load(() -> {
            latch.countDown();
        }, event.getUniqueId());
        try {
            latch.await(100, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("An error have ...");
            //TODO add some logging
        }
    }

}
