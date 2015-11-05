package com.lostshard.Lostshard.Skills;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.ItemUtils;
import com.lostshard.Lostshard.Utils.Output;

public class BladesSkill extends Skill {

	public static void playerDamagedEntityWithSword(
			EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof LivingEntity))
			return;
		final Player player = (Player) event.getDamager();
		final Material item = player.getItemInHand().getType();
		if (!ItemUtils.isSword(item))
			return;
		final Entity damagedEntity = event.getEntity();
		final PseudoPlayer pPlayer = pm.getPlayer(player);
		final Skill skill = pPlayer.getCurrentBuild().getBlades();
		final int swordsSkill = skill.getLvl();
		double damage = event.getDamage();
		int additionalDamage = 0;
		
		additionalDamage = Math.floorDiv(skill.getLvl(), 250);

		if (swordsSkill >= 250) {
			final double chanceOfEffect = (double) swordsSkill / 1000;
			final double bleedChance = chanceOfEffect * .2;

			if (bleedChance > Math.random())
				if (damagedEntity instanceof Player) {
					final Player defenderPlayer = (Player) damagedEntity;
					final PseudoPlayer defenderPseudoPlayer = pm
							.getPlayer(defenderPlayer);
					if (defenderPseudoPlayer.getTimer().bleedTick <= 0) {
						defenderPseudoPlayer.getTimer().bleedTick = 10;
						defenderPlayer.sendMessage(ChatColor.GREEN
								+ "You are bleeding!");
						player.sendMessage(ChatColor.GREEN
								+ defenderPlayer.getName() + " is bleeding!");
					}
				}
		}

		damage += additionalDamage;
		if (event.isApplicable(DamageModifier.BASE))
			event.setDamage(DamageModifier.BASE, damage);

		if (damagedEntity instanceof Monster || damagedEntity instanceof Player
				|| damagedEntity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);
		final int gain = pPlayer.getCurrentBuild().getBlades()
				.skillGain(pPlayer);
		Output.gainSkill(player, "Blades", gain, skill.getLvl());
		if (gain > 0)
			pPlayer.update();
	}

	public BladesSkill() {
		super();
		this.setName("Blades");
		this.setBaseProb(.2);
		this.setScaleConstant(60);
		this.setMat(Material.IRON_SWORD);
	}

	@Override
	public String howToGain() {
		return "You can gain blades by hitting and killing mobs";
	}
}
