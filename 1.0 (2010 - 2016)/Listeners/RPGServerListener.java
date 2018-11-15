package com.lostshard.RPG.Listeners;

import java.util.ArrayList;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.MapInitializeEvent;
import org.bukkit.map.MapView;

import com.lostshard.RPG.RPG;
import com.lostshard.RPG.TreasureMap;

/**
 * RPG block listener
 * @author luciddream
 */
public class RPGServerListener implements Listener {
	@SuppressWarnings("unused")
	private final RPG plugin;
	
	ArrayList<TreasureMap> _treasureMaps;

    public RPGServerListener(RPG plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onMapInitialize(MapInitializeEvent event) {
    	MapView mapView = event.getMap();
    	
    	short mapId = mapView.getId();
    	
    }
}