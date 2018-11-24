package com.lostshard.RPG.Skills;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.*;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class ActiveBandage {
	private static final double MAX_BANDAGE_DISTANCE = 3.0;
	private String _healerPlayerName;
	private LivingEntity _bandagedLivingEntity = null;
	private boolean _healSelf;
	private int _healSkill;
	private double _healTime;
	private double _curTime;
	private boolean _isDead = false;
	private String _creatureName;
	private int _creatureMaxHealth;
	private int _interruptCount = 0;
	
	public ActiveBandage(String healerPlayerName, int healSkill, LivingEntity bandagedLivingEntity, String creatureName, int creatureMaxHealth) {
		_healerPlayerName = healerPlayerName;
		_healSkill = healSkill;
		_bandagedLivingEntity = bandagedLivingEntity;
		_creatureName = creatureName;
		_creatureMaxHealth = creatureMaxHealth;
		
		_healTime = 3;
		_curTime = 0;
		
		_healSelf = false;
	}
	
	public ActiveBandage(String healerPlayerName, int healSkill) {
		_healerPlayerName = healerPlayerName;
		_healSkill = healSkill;
		
		_healTime = 5;
		_curTime = 0;
		
		_healSelf = true;
	}
	
	public String getHealerPlayer() {
		return _healerPlayerName;
	}
	
	public LivingEntity getBandagedLivingEntity() {
		return _bandagedLivingEntity;
	}
	
	public boolean isDead() {
		return _isDead;
	}
	
	public String getCreatureName() {
		return _creatureName;
	}
	
	public void bandagerHit(Player player) {
		player.sendMessage(ChatColor.GRAY+"Your fingers slip while applying the bandage.");
		_interruptCount++;
		_healTime += .5;
	}
	
	public void tick(double delta) {
		if(_isDead == true)
			return;
		
		_curTime += (delta/10);
		if(_curTime >= _healTime) {
			if(_healSelf) {
				Player healerPlayer = Bukkit.getServer().getPlayer(_healerPlayerName);
				if(healerPlayer != null) {
					double healAmount = (int)Math.floor(((double)_healSkill / 100) + 2);
					Damageable damag = healerPlayer;
					double curHealth = damag.getHealth();
					if(curHealth + healAmount > 20) {
						healAmount = 20 - curHealth;
					}
					if(healAmount <= 0) {
						healerPlayer.sendMessage(ChatColor.GRAY+"You finish applying the bandage but you no longer needed healing.");
						PseudoPlayer healerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(healerPlayer.getName());
						if(healerPseudoPlayer != null)
							healerPseudoPlayer._activeBandage = null;
						_isDead = true;
					}
					else {
						healAmount -= _interruptCount;
						if(healAmount < 1)
							healAmount = 1;
						if(healAmount % 2 == 0)
							healerPlayer.sendMessage(ChatColor.GOLD+"You heal yourself for "+healAmount/2+" hearts.");
						else
							healerPlayer.sendMessage(ChatColor.GOLD+"You heal yourself for "+healAmount/2+" and a half hearts.");
						
						healerPlayer.setHealth(damag.getHealth()+healAmount);
						PseudoPlayer healerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(healerPlayer.getName());
						if(healerPseudoPlayer != null)
							healerPseudoPlayer._activeBandage = null;
						_isDead = true;
					}
				}
			}
			else {
				if(_bandagedLivingEntity != null && _creatureName != null && _creatureMaxHealth > 0) {
					double healAmount = (int)Math.floor(((double)_healSkill / 100) + 2);
					Damageable damag = _bandagedLivingEntity;
					double curHealth = damag.getHealth();
					if(curHealth + healAmount > _creatureMaxHealth) {
						healAmount = _creatureMaxHealth - curHealth;
					}
					if(healAmount <= 0) {
						Player healerPlayer = Bukkit.getServer().getPlayer(_healerPlayerName);
						if(healerPlayer != null)
							healerPlayer.sendMessage(ChatColor.GRAY+"You finish applying the bandage but the target no longer needs healing.");
						PseudoPlayer healerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(healerPlayer.getName());
						if(healerPseudoPlayer != null)
							healerPseudoPlayer._activeBandage = null;
						_isDead = true;
					}
					else {
						Player healerPlayer = Bukkit.getServer().getPlayer(_healerPlayerName);
						if(healerPlayer != null) {
							healAmount -= _interruptCount;
							if(healAmount < 1)
								healAmount = 1;
							if(healAmount % 2 == 0)
								healerPlayer.sendMessage(ChatColor.GOLD+"You heal "+_creatureName+" for "+healAmount/2+" hearts.");
							else
								healerPlayer.sendMessage(ChatColor.GOLD+"You heal "+_creatureName+" for "+healAmount/2+" and a half hearts.");
						
							if(_bandagedLivingEntity instanceof Player) {
								Player bPlayer = (Player)_bandagedLivingEntity;
								if(healAmount % 2 == 0)
									bPlayer.sendMessage(ChatColor.GOLD+"You were healed for "+healAmount/2+" hearts by "+healerPlayer.getName()+".");
								else
									bPlayer.sendMessage(ChatColor.GOLD+"You were healed for "+healAmount/2+" and a half hearts by "+healerPlayer.getName()+".");
							}
							
							if(damag.getHealth()+healAmount > _creatureMaxHealth || damag.getHealth()+healAmount < 0) {}
							else {
								if(damag.getHealth() + healAmount > 200 || damag.getHealth() + healAmount < 0) {}
								else {
									try {
										_bandagedLivingEntity.setHealth(damag.getHealth() + healAmount);
									}
									catch(Exception e) {
										System.out.println("BANDAGERROR");
									}
								}
							}
						}
						PseudoPlayer healerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(healerPlayer.getName());
						if(healerPseudoPlayer != null)
							healerPseudoPlayer._activeBandage = null;
						_isDead = true;
					}
				}
			}
			_isDead = true;
		}
		else {
			if(!_healSelf) {
				Player player = Bukkit.getServer().getPlayer(_healerPlayerName);
				double distanceBetween = Utils.distance(player.getLocation(), _bandagedLivingEntity.getLocation());
				if(distanceBetween > MAX_BANDAGE_DISTANCE) {
					Output.simpleError(player, "You are too far away from your target so you failed to bandage them.");
					if(_bandagedLivingEntity instanceof Player) {
						Player bPlayer = (Player)_bandagedLivingEntity;
						Output.simpleError(bPlayer, "You are too far away from "+player.getName()+" so their attempt to bandage you failed.");
					}
					PseudoPlayer healerPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
					if(healerPseudoPlayer != null)
						healerPseudoPlayer._activeBandage = null;
					_isDead = true;
				}
			}
		}
	}
}
