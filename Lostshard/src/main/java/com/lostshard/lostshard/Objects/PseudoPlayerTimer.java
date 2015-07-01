package com.lostshard.lostshard.Objects;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Spells.Spells.SPL_Chronoport;
import com.lostshard.lostshard.Utils.Output;

public class PseudoPlayerTimer {

	public int recentlyTeleportedTicks;

	private PseudoPlayer player;

	// Effects
	public int bleedTick = 0;
	public int stunTick = 0;

	public long lastDeath = 0;
	public int goToSpawnTicks = 0;

	public int spawnTicks = 0;

	public int cantCastTicks = 0;

	public Spell delayedSpell = null;

	public SPL_Chronoport chronoport = null;

	public PseudoPlayerTimer(PseudoPlayer pPlayer) {
		super();
		this.player = pPlayer;
	}

	private void bleed() {
		if (this.bleedTick > 0) {
			final Player p = this.player.getOnlinePlayer();
			this.bleedTick--;

			if (this.bleedTick <= 0) {
				p.sendMessage(ChatColor.GREEN+"Your bleeding has stopped.");
				this.bleedTick = 0;
			} else {
				double newHealth = p.getHealth() - 1;
				if (newHealth > 20)
					newHealth = 20;
				if (newHealth < 0)
					newHealth = 0;
				p.setHealth(newHealth);
			}
		}
	}

	public PseudoPlayer getPlayer() {
		return this.player;
	}

	public boolean isLastDeathOlder(long ms) {
		return new Date().getTime() > this.lastDeath + ms;
	}

	private void recentAttackersTick() {
		for (final RecentAttacker ra : this.player.getRecentAttackers())
			ra.tick();
		this.player.getRecentAttackers().removeIf(ra -> ra.isDead());
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}

	private void spawn() {
		if (this.goToSpawnTicks > 0) {
			this.goToSpawnTicks--;
			if (this.goToSpawnTicks == 0) {
				final Player player = this.player.getOnlinePlayer();
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.teleport(this.getPlayer().getSpawn());
				this.spawnTicks = 36000;
				this.player.setMana(0);
				this.player.setStamina(0);
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.sendMessage(ChatColor.GRAY
						+ "Teleporting without a rune has exausted you.");
			} else if (this.goToSpawnTicks % 10 == 0) {
				final Player player = this.player.getOnlinePlayer();
				Output.simpleError(player, "Returning to spawn in "
						+ this.goToSpawnTicks / 10 + " seconds.");
			}
		}
	}

	public void tick(double delta, long tick) {
		if (this.player.getOnlinePlayer() == null)
			return;
		if (tick % 10 == 0) { // one second passed
			this.updateMana(delta);
			this.updateStamina(delta);
		}
		if(tick % 20 == 0) {
			this.bleed();	
		}
		if (this.cantCastTicks > 0)
			this.cantCastTicks--;
		this.recentAttackersTick();
		if (this.spawnTicks > 0)
			this.spawnTicks--;
		if (this.player.getCriminal() > 0) {
			this.player.setCriminal(this.player.getCriminal() - 1);
			if(this.player.getCriminal() <= 0)
				this.player.getScoreboard().updateTeams();
		}
		if (this.player.getPvpTicks() > 0)
			this.player.setPvpTicks(this.player.getPvpTicks() - 1);
		if (this.player.getEngageInCombatTicks() > 0)
			this.player.setEngageInCombatTicks(this.player.getEngageInCombatTicks() - 1);
		if (this.delayedSpell != null && this.player.getPromptedSpell() == null)
			this.delayedSpell.tick(this.player.getOnlinePlayer());
		if (this.chronoport != null)
			this.chronoport.chronoTick();
		if (this.recentlyTeleportedTicks > 0)
			this.recentlyTeleportedTicks--;
		if(this.stunTick > 0)
			this.stunTick --;
		this.spawn();
	}

	private void updateMana(double delta) {
		if (this.player.getMana() < this.player.getMaxMana()) {
			final double manaRegenMultiplier = 2; // Meditation.getManaRegenMultiplier(this);
			if (this.player.isMeditating())
				this.player.setMana((int) (this.player.getMana() + 2
						* manaRegenMultiplier * delta));
			else
				this.player.setMana((int) (this.player.getMana() + 1
						* manaRegenMultiplier * delta));
			if (this.player.getMana() >= this.player.getMaxMana()) {
				this.player.setMana(this.player.getMaxMana());
				;
				// just reached max...
				final Player p = this.player.getOnlinePlayer();
				if (p != null)
					Output.positiveMessage(p,
							"Your mana has fully regenerated.");
			}
		}
	}

	private void updateStamina(double delta) {
		if (this.player.getStamina() < this.player.getMaxStamina()) {
			final double staminaRegenMultiplier = 1;
			if (this.player.isResting())
				this.player.setStamina((int) (this.player.getStamina() + 2
						* staminaRegenMultiplier * delta));
			else
				this.player.setStamina((int) (this.player.getStamina() + 1
						* staminaRegenMultiplier * delta));
			if (this.player.getStamina() >= this.player.getMaxStamina()) {
				this.player.setStamina(this.player.getMaxStamina());
				;
				// just reached max...
				final Player p = this.player.getOnlinePlayer();
				if (p != null)
					Output.positiveMessage(p,
							"Your stamina has fully regenerated.");
			}
		}
	}
}
