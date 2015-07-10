package com.lostshard.Lostshard.Skills;

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

import com.lostshard.Lostshard.Handlers.PVPHandler;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

public class LumberjackingSkill extends Skill {

	@SuppressWarnings("deprecation")
	public static void blockBrokeWithAxe(BlockBreakEvent event) {
		if (event.isCancelled())
			return;
		final Player player = event.getPlayer();
		if (!ItemUtils.isAxe(player.getItemInHand()))
			return;
		final Block block = event.getBlock();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getLumberjacking();
		final int lumberSkill = skill.getLvl();
		final double chanceToDropPlank = (double) lumberSkill / 1000;
		byte data;
		if (block.getType().equals(Material.LOG)
				&& !block.hasMetadata("placed")) {
			if (block.getData() == 1 || block.getData() == 5
					|| block.getData() == 9 || block.getData() == 13)
				data = 1;
			else if (block.getData() == 2 || block.getData() == 6
					|| block.getData() == 10 || block.getData() == 14)
				data = 2;
			else if (block.getData() == 3 || block.getData() == 7
					|| block.getData() == 11 || block.getData() == 15)
				data = 3;
			else
				data = 0;

			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));

			skill.setBaseProb(.3);
			skill.setScaleConstant(80);
			final int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
		} else if (block.getType().equals(Material.LOG_2)
				&& !block.hasMetadata("placed")) {
			if (block.getData() == 1 || block.getData() == 5
					|| block.getData() == 9 || block.getData() == 13)
				data = 5;
			else
				data = 4;
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));
			if (Math.random() < chanceToDropPlank)
				block.getWorld().dropItemNaturally(block.getLocation(),
						new ItemStack(Material.WOOD, 1, data));

			skill.setBaseProb(.3);
			skill.setScaleConstant(80);
			final int gain = skill.skillGain(pPlayer);
			Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
		}
	}

	public static void playerDamagedEntityWithAxe(
			EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		final Player player = (Player) event.getDamager();
		if (!ItemUtils.isAxe(player.getItemInHand()))
			return;
		final Entity damagedEntity = event.getEntity();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getLumberjacking();
		final int lumberjackingSkill = skill.getLvl();
		double damage = event.getDamage();
		final int additionalDamage = 0;

		double damagePercent = 1;
		if (lumberjackingSkill >= 1000)
			damagePercent += .5;
		else if (lumberjackingSkill >= 750)
			damagePercent += .35;
		else if (lumberjackingSkill >= 500)
			damagePercent += .25;
		else if (lumberjackingSkill >= 250)
			damagePercent += .15;

		damage += additionalDamage;

		final List<Entity> nearbyEntities = damagedEntity.getNearbyEntities(3,
				3, 3);
		final double newDamage = (int) Math.ceil(damage * damagePercent);
		for (final Entity entity : nearbyEntities) {
			if (entity == damagedEntity)
				continue;
			if (entity instanceof LivingEntity) {
				final LivingEntity cleavedEntity = (LivingEntity) entity;
				if (cleavedEntity != null)
					if (cleavedEntity instanceof Player) {
						final Player p = (Player) cleavedEntity;
						if (p != player
								&& PVPHandler.canEntityAttackEntity(player,
										damagedEntity)) {
							// RPGEntityListener.criminalAction(p, player);
							final double adjustedDamage = Utils
									.adjustDamageForArmor(p, newDamage);
							cleavedEntity.damage(adjustedDamage);
						}
					} else
						cleavedEntity.damage(newDamage);
			}
		}

		event.setDamage(damage);

		if (damagedEntity instanceof Monster || damagedEntity instanceof Player
				|| damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		final int gain = skill.skillGain(pPlayer);
		Output.gainSkill(player, "Lumberjacking", gain, skill.getLvl());
		if (gain > 0)
			pPlayer.update();
	}

	public LumberjackingSkill() {
		super();
		this.setName("Lumberjacking");
		this.setBaseProb(.2);
		this.setScaleConstant(60);
		this.setMat(Material.IRON_AXE);
	}

	@Override
	public String howToGain() {
		return "You can gain lumberjacking by choping down trees or hitting or killing mobs";
	}

}
