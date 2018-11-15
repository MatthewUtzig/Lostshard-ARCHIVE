package com.lostshard.RPG.Listeners;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.plugin.Plugin;

import com.lostshard.RPG.PseudoWolf;
import com.lostshard.RPG.PseudoWolfHandler;
import com.lostshard.RPG.RPG;

public class RPGWorldListener implements Listener{
	Plugin plugin;
	
	public RPGWorldListener(RPG instance) {
    	plugin = instance;
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		Chunk chunk = event.getChunk();
		Entity[] entities = chunk.getEntities();
		for(Entity e : entities) {
			if(e instanceof Wolf) {
				Wolf wolf = (Wolf)e;
				if(wolf.isTamed()) {
					PseudoWolf pseudoWolf = PseudoWolfHandler.getPseudoWolf(wolf.getUniqueId().toString());
					if(pseudoWolf == null) {
						wolf.setTamed(false);
						wolf.damage(2000);
					}
					else {
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		if(RPG._permChunks.contains(event.getChunk().toString())) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
    public void onPortalCreate(PortalCreateEvent event) {		
    	/*ArrayList<Block> portalBlocks = event.getBlocks();
    	String blockString = event.getWorld().getName()+"#";
    	int numBlocks = portalBlocks.size();
    	for(int i=0; i<numBlocks; i++) {
    		Block b = portalBlocks.get(i);
    		blockString+=(b.getX()+"-"+b.getY()+"-"+b.getZ());
    		if(i < numBlocks-1)
    			blockString+="#";
    	}
    	
    	System.out.println("Portal String: " +blockString);*/
    }
}
