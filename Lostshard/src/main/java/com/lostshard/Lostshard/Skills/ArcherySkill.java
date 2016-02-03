package com.lostshard.Lostshard.Skills;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.Output;

@Embeddable
public class ArcherySkill extends Skill {

	public static void EntityDamageByEntityEvent(EntityDamageByEntityEvent event) {
		if (event.isCancelled())
			return;
		if(!(event.getEntity() instanceof LivingEntity))
			return;
		if (!(event.getDamager() instanceof Arrow))
			return;
		final Arrow arrow = (Arrow) event.getDamager();
		if (!(arrow.getShooter() instanceof Player))
			return;

		final Player attacker = (Player) arrow.getShooter();
		final Entity entity = event.getEntity();

		final PseudoPlayer pPlayer = pm.getPlayer(attacker);

		final Skill skill = pPlayer.getCurrentBuild().getArchery();
		final int lvl = skill.getLvl();

		final double damage = skill.getLvl() / 250;

		if (entity instanceof Player && lvl >= 500 && Math.random() < .25 && ((Player) entity).getHealth() > 4
				&& Math.abs(event.getDamage(DamageModifier.ARMOR)
						+ event.getDamage(DamageModifier.MAGIC)) >= event.getDamage(DamageModifier.BASE) * .35) {
			
			final Player player = (Player) event.getEntity();
			double health = player.getHealth() - (event.getDamage(DamageModifier.BASE) * .90 + damage * .6);
			health = Math.max(health, 4);
			if(health < event.getFinalDamage()) {
				
				player.setHealth(health);
				event.setDamage(0);
				player.sendMessage(ChatColor.GREEN + "The arrow pierces through your armor!");
				attacker.sendMessage(ChatColor.GREEN + "Your arrow pierced through " + player.getName() + "'s armor.");
			}
		} else if (event.isApplicable(DamageModifier.BASE))
			event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE) + damage);

		if (entity instanceof Monster || entity instanceof Player || entity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);

		final int gain = skill.skillGain(pPlayer);
		Output.gainSkill(attacker, "Archery", gain, skill.getLvl());
		if (gain > 0)
			pPlayer.update();
	}

	public ArcherySkill() {
		super();
		this.setName("Archery");
		this.setBaseProb(1);
		this.setScaleConstant(50);
		this.setMinGain(5);
		this.setMinGain(15);
		this.setMat(Material.BOW);
	}

	public ArcherySkill(int lvl, boolean locked) {
		super(lvl, locked);
	}

	@Transient
	public double getDamageBuff() {
		return 0.004 * this.getLvl();
	}

	@Override
	public String howToGain() {
		return "You can gain archery by shooting and killing mobs";
	}

}
