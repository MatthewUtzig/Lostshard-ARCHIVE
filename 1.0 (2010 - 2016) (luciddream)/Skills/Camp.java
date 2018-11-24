package com.lostshard.RPG.Skills;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.lostshard.RPG.Utils.Utils;

public class Camp {
	private double _campHealDistance = 5;
	private String _creator;
	private int _ticksRemaining;
	public boolean _isDead = false;
	public Block _logBlock;
	public Block _fireBlock;
	
	public Camp(String creator, int ticks, Block log, Block fire) {
		_creator = creator;
		_ticksRemaining = ticks;
		_logBlock = log;
		_fireBlock = fire;
	}
	
	public void tick() {
		if(!_isDead) {
			boolean doused = false;
			_ticksRemaining--;
			if(_ticksRemaining <= 0) {
				Player creatorPlayer = Utils.getPlugin().getServer().getPlayer(_creator);
				if(creatorPlayer != null) {
					creatorPlayer.sendMessage(ChatColor.GRAY+"Your camp fire has gone out.");
				}
	
				Utils.loadChunkAtLocation(_logBlock.getLocation());
				_fireBlock.setType(Material.AIR);
				_logBlock.setType(Material.AIR);
				
				_isDead = true;
				
			}
			else if(!_logBlock.getWorld().getBlockAt(_logBlock.getX(), _logBlock.getY(), _logBlock.getZ()).getType().equals(Material.LOG)) {
				doused = true;
			}
			else if(!_fireBlock.getWorld().getBlockAt(_fireBlock.getX(), _fireBlock.getY(), _fireBlock.getZ()).getType().equals(Material.FIRE)) {
				doused = true;
			}
			if(doused) {
				Player creatorPlayer = Utils.getPlugin().getServer().getPlayer(_creator);
				if(creatorPlayer != null) {
					creatorPlayer.sendMessage(ChatColor.GRAY+"Your camp fire has been doused.");
				}
				
				Utils.loadChunkAtLocation(_logBlock.getLocation());
				_fireBlock.setType(Material.AIR);
				_logBlock.setType(Material.AIR);
				_isDead = true;
			}
			if(!_isDead) {
				if(_ticksRemaining % 20 == 0) {
					campHealTick();
				}
			}
		}
	}
	
	public String getCreator() {
		return _creator;
	}
	
	public void campHealTick() {
		Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		Location loc = _logBlock.getLocation();
		for(Player p : onlinePlayers) {
			if(Utils.isWithin(p.getLocation(), loc, _campHealDistance)) {
				Damageable damag = p;
				if(damag.getHealth() > 0 && damag.getHealth() < 20) 
				{
					double newHealth = damag.getHealth() + 1;
					if(newHealth > 20)
						newHealth = 20;
					if(newHealth < 0)
						newHealth = 0;
					p.setHealth(newHealth);
				}
			}
		}
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public Block getLogBlock() {
		return _logBlock;
	}
	
	public Block getFireBlock() {
		return _fireBlock;
	}
}
