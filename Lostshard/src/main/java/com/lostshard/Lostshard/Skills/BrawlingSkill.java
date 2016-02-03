package com.lostshard.Lostshard.Skills;

import javax.persistence.Embeddable;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;

@Embeddable
public class BrawlingSkill extends Skill {

	public static void playerDamagedEntityWithMisc(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		if (event.getEntity().getType().equals(EntityType.ARMOR_STAND))
			return;
		final Player player = (Player) event.getDamager();
		final Material item = player.getItemInHand().getType();
		if (ItemUtils.isSword(item) || ItemUtils.isAxe(item))
			return;
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getBrawling();
		final Entity damagedEntity = event.getEntity();
		final int brawlingSkill = skill.getLvl();
		double damage = event.getDamage();
		final int additionalDamage = skill.getLvl() / 160;

		final double chanceOfEffect = (double) brawlingSkill / 1000;
		final double stunChance = chanceOfEffect * .25;

		final double rand = Math.random();
		if (stunChance > rand)
			if (damagedEntity instanceof Player) {
				final Player defenderPlayer = (Player) damagedEntity;
				final PseudoPlayer defenderPseudoPlayer = pm.getPlayer(defenderPlayer);
				if (defenderPseudoPlayer.getTimer().stunTick <= 0) {
					defenderPseudoPlayer.getTimer().stunTick = 30;
					defenderPlayer.sendMessage(ChatColor.GREEN + "You have been stunned!");
					player.sendMessage(ChatColor.GREEN + "You stunned " + defenderPlayer.getName() + "!");
				}
			}

		if (damagedEntity instanceof Player)
			if (brawlingSkill >= 1000) {
				final Player defenderPlayer = (Player) damagedEntity;
				final PseudoPlayer defenderPseudoPlayer = pm.getPlayer(defenderPlayer);
				if (defenderPseudoPlayer.getTimer().stunTick <= 0)
					defenderPseudoPlayer.getTimer().stunTick = 17;
			}

		damage += additionalDamage;
		event.setDamage(damage);

		if (damagedEntity instanceof Monster || damagedEntity instanceof Player || damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		final int gain = skill.skillGain(pPlayer);
		Output.gainSkill(player, "Brawling", gain, skill.getLvl());
		if (gain > 0)
			pPlayer.update();
	}

	public BrawlingSkill() {
		super();
		this.setName("Brawling");
		this.setBaseProb(1);
		this.setScaleConstant(35);
		this.setMaxGain(15);
		this.setMinGain(5);
		this.setMat(Material.PORK);
	}

	public BrawlingSkill(int lvl, boolean locked) {
		super(lvl, locked);
	}

	@Override
	public String howToGain() {
		return "You can gain brawling by hitting or killing mobs";
	}

}
