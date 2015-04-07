package com.lostshard.lostshard.Objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Utils.Utils;

public class Camp {

	private double campHealDistance = 5;
	private UUID creator;
	private int ticksRemaining;
	public boolean isDead = false;
	public Block logBlock;
	public Block fireBlock;
	
	public Camp(UUID creator, int ticks, Block log, Block fire) {
		this.creator = creator;
		ticksRemaining = ticks;
		logBlock = log;
		fireBlock = fire;
	}
	
	public void tick() {
		if(!isDead) {
			boolean doused = false;
			ticksRemaining--;
			if(ticksRemaining <= 0) {
				Player creatorPlayer = Bukkit.getPlayer(creator);
				if(creatorPlayer != null) {
					creatorPlayer.sendMessage(ChatColor.GRAY+"Your camp fire has gone out.");
				}
	
				fireBlock.setType(Material.AIR);
				logBlock.setType(Material.AIR);
				
				isDead = true;
				
			}
			else if(!logBlock.getWorld().getBlockAt(logBlock.getX(), logBlock.getY(), logBlock.getZ()).getType().equals(Material.LOG)) {
				doused = true;
			}
			else if(!fireBlock.getWorld().getBlockAt(fireBlock.getX(), fireBlock.getY(), fireBlock.getZ()).getType().equals(Material.FIRE)) {
				doused = true;
			}
			if(doused) {
				Player creatorPlayer = Bukkit.getPlayer(creator);
				if(creatorPlayer != null) {
					creatorPlayer.sendMessage(ChatColor.GRAY+"Your camp fire has been doused.");
				}
				
				fireBlock.setType(Material.AIR);
				logBlock.setType(Material.AIR);
				isDead = true;
			}
			if(!isDead) {
				if(ticksRemaining % 20 == 0) {
					campHealTick();
				}
			}
		}
	}
	
	public UUID getCreator() {
		return creator;
	}
	
	public void campHealTick() {
		Location loc = logBlock.getLocation();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(Utils.isWithin(p.getLocation(), loc, campHealDistance)) {
				if(p.getHealth() > 0 && p.getHealth() < 20) 
				{
					double newHealth = Math.min(Math.max(p.getHealth() + 1, 0),20);
					p.setHealth(newHealth);
				}
			}
		}
	}
	
	public boolean isDead() {
		return isDead;
	}
	
	public Block getLogBlock() {
		return logBlock;
	}
	
	public Block getFireBlock() {
		return fireBlock;
	}


}
