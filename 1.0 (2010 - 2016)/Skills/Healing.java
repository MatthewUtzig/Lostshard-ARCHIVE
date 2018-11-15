package com.lostshard.RPG.Skills;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Squid;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Healing {
	private static ArrayList<ActiveBandage> _activeBandages = new ArrayList<ActiveBandage>();
	private static final int BANDAGE_STAMINA_COST  = 30;
	
	public static void tick(double delta) {
		int numBandages = _activeBandages.size();
		for(int i=numBandages-1; i>=0; i--) {
			ActiveBandage activeBandage = _activeBandages.get(i);
			activeBandage.tick(delta);
			if(activeBandage.isDead()) {
				_activeBandages.remove(i);
			}
		}
	}
	
	public static void playerRegainHealth(Player player, EntityRegainHealthEvent event) {
		
	}
	
	public static void playerBandageSelf(Player player) {
		if(isPlayerBandaging(player)) {
			Output.simpleError(player, "You are already bandaging something.");
			return;
		}
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		if(pseudoPlayer.getStamina() < BANDAGE_STAMINA_COST) {
			Output.simpleError(player, "You do not have enough stamina to bandage yourself, it requires "+BANDAGE_STAMINA_COST+".");
		}
		else {
			pseudoPlayer.setStamina(pseudoPlayer.getStamina()-BANDAGE_STAMINA_COST);
				
			int healSkill = pseudoPlayer.getSkill("healing");
			ActiveBandage activeBandage = createBandageSelf(player, healSkill);
			player.sendMessage(ChatColor.GRAY+"You begin to bandage yourself...");
			ItemStack itemInHand = player.getItemInHand();
			int numInStack = itemInHand.getAmount();
			if(numInStack > 1)
				itemInHand.setAmount(numInStack-1);
			else
				player.getInventory().clear(player.getInventory().getHeldItemSlot());
			_activeBandages.add(activeBandage);
			pseudoPlayer._activeBandage = activeBandage;
		}
	}
	
	public static void playerBandageEntity(Player player, Entity bandagedEntity) {
		if(isPlayerBandaging(player)) {
			Output.simpleError(player, "You are already bandaging something.");
			return;
		}
		
		if(Utils.distance(player.getLocation(), bandagedEntity.getLocation()) > BANDAGE_STAMINA_COST) {
			Output.simpleError(player, "You are too far from that to heal it.");
			return;
		}
		
		if(bandagedEntity instanceof LivingEntity) {
			LivingEntity bandagedLivingEntity = (LivingEntity)bandagedEntity;
			PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
			int healSkill = pseudoPlayer.getSkill("healing");
			ActiveBandage activeBandage = createBandageOther(player, pseudoPlayer, healSkill, bandagedLivingEntity);
			if(activeBandage == null) {
				Output.simpleError(player,  "That creature cannot be bandaged.");
			}
			else {
				if(pseudoPlayer.getStamina() < BANDAGE_STAMINA_COST) {
					Output.simpleError(player, "You do not have enough stamina to bandage "+activeBandage.getCreatureName()+", it requires "+BANDAGE_STAMINA_COST+".");
				}
				else {
					pseudoPlayer.setStamina(pseudoPlayer.getStamina()-BANDAGE_STAMINA_COST);
					player.sendMessage(ChatColor.GRAY+"You begin to bandage "+activeBandage.getCreatureName()+"...");
					if(bandagedEntity instanceof Player) {
						Player bPlayer = (Player)bandagedEntity;
						bPlayer.sendMessage(ChatColor.GRAY+player.getName()+" begins to bandage you...");
					}
					ItemStack itemInHand = player.getItemInHand();
					int numInStack = itemInHand.getAmount();
					if(numInStack > 1)
						itemInHand.setAmount(numInStack-1);
					else
						player.getInventory().clear(player.getInventory().getHeldItemSlot());
					_activeBandages.add(activeBandage);
					pseudoPlayer._activeBandage = activeBandage;
				}
			}
		}
	}
	
	public static ActiveBandage createBandageSelf(Player healerPlayer, int healSkill) {
		return new ActiveBandage(healerPlayer.getName(), healSkill);
	}
	
	public static boolean isPlayerBandaging(Player player) {
		for(ActiveBandage ab : _activeBandages) {
			if(ab.getHealerPlayer().equals(player.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public static ActiveBandage createBandageOther(Player healerPlayer, PseudoPlayer pseudoPlayer, int healSkill, LivingEntity bandagedLivingEntity) {
		String creatureName = null;
		int creatureMaxHealth = -1;
		
		if(bandagedLivingEntity instanceof Zombie) {
			creatureName = "a Zombie";
			creatureMaxHealth = 20;
		}
		else if(bandagedLivingEntity instanceof Skeleton) {
			creatureName = "a Skeleton";
			creatureMaxHealth = 20;
		}
		else if(bandagedLivingEntity instanceof Spider) {
			creatureName = "a Spider";
			creatureMaxHealth = 20;
		}
		else if(bandagedLivingEntity instanceof Creeper) {
			creatureName = "a Creeper";
			creatureMaxHealth = 20;
		}
		else if(bandagedLivingEntity instanceof Pig) {
			creatureName = "a Pig";
			creatureMaxHealth = 10;
		}
		else if(bandagedLivingEntity instanceof Sheep) {
			creatureName = "a Sheep";
			creatureMaxHealth = 10;
		}
		else if(bandagedLivingEntity instanceof Cow) {
			creatureName = "a Cow";
			creatureMaxHealth = 10;
		}
		else if(bandagedLivingEntity instanceof Chicken) {
			creatureName = "a Chicken";
			creatureMaxHealth = 4;
		}
		else if(bandagedLivingEntity instanceof Squid) {
			creatureName = "a Squid";
			creatureMaxHealth = 10;
		}
		else if(bandagedLivingEntity instanceof Wolf) {
			Wolf wolf = (Wolf)bandagedLivingEntity;
				if(wolf.isTamed()) {
				Player ownerPlayer = (Player)wolf.getOwner();
				if(ownerPlayer != null) {
					creatureName = ownerPlayer.getName()+"'s Wolf";
				}
				else {
					creatureName = "a Wolf";
				}
				creatureMaxHealth = 20;
			}
			else {
				creatureName = "a Wolf";
				creatureMaxHealth = 8;
			}
		}
		else if(bandagedLivingEntity instanceof Player) {
			Player player = (Player)bandagedLivingEntity;
			creatureName = player.getName();
			creatureMaxHealth = 20;
		}
		
		if(creatureName == null || creatureMaxHealth <= 0) {
			return null;
		}
		
		ActiveBandage activeBandage = new ActiveBandage(healerPlayer.getName(), healSkill, bandagedLivingEntity, creatureName, creatureMaxHealth);
		return activeBandage;
	}
}
