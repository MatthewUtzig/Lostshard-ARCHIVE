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

		final double damage = skill.getLvl() / 250;
		
		if(!(entity instanceof Player) || !(pierce((Player) entity, attacker, event)))
			event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE) + damage);

		if (entity instanceof Monster || entity instanceof Player || entity instanceof Slime)
			skill.setBaseProb(.5);
		else
			skill.setBaseProb(.2);

		final int gain = skill.skillGain(pPlayer);
		Output.gainSkill(attacker, "Archery", gain, skill.getLvl());
	}
	
	private static boolean pierce(Player player, Player attacker, EntityDamageByEntityEvent event) {
		final double armor = event.getOriginalDamage(DamageModifier.ARMOR);
		
		double damage = event.getDamage(DamageModifier.BASE);
		
		double chance = 1-(armor+4)/damage;
		
		damage *= .75;
		
		damage = Math.min(damage, 15);
		
		if (event.getFinalDamage() > damage)
			return false;
		
		double health = player.getHealth();
		
		if(event.getFinalDamage() >= health)
			return false;
				
		health = Math.max(1, health-damage);
		
		if(chance > Math.random()) {
			if(event.getFinalDamage() < 1 && chance < .5)
				event.setDamage(DamageModifier.BASE, event.getDamage(DamageModifier.BASE)+(1-event.getFinalDamage()));
			return false;
		}
		player.setHealth(health);
		event.setDamage(0);
		player.sendMessage(ChatColor.GREEN + "The arrow pierces through your armor!");
		attacker.sendMessage(ChatColor.GREEN + "Your arrow pierced through " + player.getName() + "'s armor.");
		return true;
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
