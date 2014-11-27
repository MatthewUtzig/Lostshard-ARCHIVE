package com.lostshard.lostshard.Skills;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Utils.ItemUtils;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class LumberjackingSkill extends Skill {

	public LumberjackingSkill() {
		super();
		setName("Lumberjacking");
		setBaseProb(.2);
		setScaleConstant(60);
	}
	
	public static void playerDamagedEntityWithAxe(EntityDamageByEntityEvent event) {
		if(event.isCancelled())
			return;
		if(!(event.getEntity() instanceof LivingEntity))
			return;
		if(!(event.getDamager() instanceof Player))
			return;
		Player player = (Player) event.getDamager();
		if(!ItemUtils.isAxe(player.getItemInHand()))
			return;
		Entity damagedEntity = event.getEntity();
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		Skill skill = pseudoPlayer.getCurrentBuild().getLumberjacking();
		int lumberjackingSkill = skill.getLvl();
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		double damagePercent = 0;
		if(lumberjackingSkill >= 1000) {
			damagePercent = .5;
		}
		else if(lumberjackingSkill >= 750) {
			damagePercent = .35;
		}
		else if(lumberjackingSkill >= 500) {
			damagePercent = .25;
		}
		else if(lumberjackingSkill >= 250) {
			damagePercent = .15;
		}
		
		damage += additionalDamage;
		
		List<Entity> nearbyEntities = damagedEntity.getNearbyEntities(3, 3, 3);
		
		for(Entity entity : nearbyEntities) {
			if(entity == damagedEntity)
				continue;
			
			if(entity instanceof LivingEntity) {
				LivingEntity cleavedEntity = (LivingEntity)entity;
				if(cleavedEntity != null) {
					int newDamage = (int)Math.ceil((damage * damagePercent));
					if(cleavedEntity instanceof Player) {
						Player p = (Player)cleavedEntity;
						if(p != player && PVPHandler.canEntityAttackEntity(player, damagedEntity)) {
							//RPGEntityListener.criminalAction(p, player);
							int adjustedDamage = Utils.adjustDamageForArmor(p, newDamage);
							cleavedEntity.damage(adjustedDamage);
						}
					}
					else
						cleavedEntity.damage(newDamage);
				}
			}
		}
		
		event.setDamage(damage);
		
		if(damagedEntity instanceof Monster || damagedEntity instanceof Player || damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		int gain = skill.skillGain();
		Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
	}
	
	public static void blockBrokeWithAxe(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
		Player player = event.getPlayer();
		if(!ItemUtils.isAxe(player.getItemInHand()))
			return;
		Block block = event.getBlock();
		PseudoPlayer pPlayer = PseudoPlayerHandler.getPlayer(player);
		Skill skill = pPlayer.getCurrentBuild().getLumberjacking();
		int lumberSkill = skill.getLvl();
		double chanceToDropPlank = (double)lumberSkill/1000;
		byte data;
		if(block.getType().equals(Material.LOG)) {
//			if(!RPG._placedLogStrings.contains(block.toString())) {
				if(block.getData() == 1 || block.getData() == 5 || block.getData() == 9 || block.getData() == 13)
					data = 1;
				else if(block.getData() == 2 || block.getData() == 6 || block.getData() == 10 || block.getData() == 14)
					data = 2;
				else if(block.getData() == 3 || block.getData() == 7 || block.getData() == 11 || block.getData() == 15)
					data = 3;
				else
					data = 0;
				
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
//			}
				skill.setBaseProb(.3);
				skill.setScaleConstant(80);
				int gain = skill.skillGain();
				Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
		}else if(block.getType().equals(Material.LOG_2)) {
//			if(!RPG._placedLogStrings.contains(block.toString())) {
				if(block.getData() == 1 || block.getData() == 5 || block.getData() == 9 || block.getData() == 13)
					data = 5;
				else
					data = 4;
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
				if(Math.random() < chanceToDropPlank)
					block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.WOOD, 1, data));
//			}
				skill.setBaseProb(.3);
				skill.setScaleConstant(80);
				int gain = skill.skillGain();
				Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
		}
	}
	
}
