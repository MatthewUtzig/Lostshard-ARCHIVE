package com.lostshard.RPG;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

public class MapListener implements Listener {
    
    private RPG plugin;

    public MapListener(RPG plugin) {
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onMapInitialize(MapInitializeEvent event) {
        MapView map = event.getMap();
        plugin.getServer().broadcastMessage("[MapListener] Map " + map.getId() + " initialized.");
        System.out.println("[MapListener] Map " + map.getId() + " initialized.");
    }
    
}