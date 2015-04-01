package com.lostshard.lostshard.Objects;

import java.util.Date;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Data.Variables;
import com.lostshard.lostshard.Objects.Recent.RecentAttacker;
import com.lostshard.lostshard.Spells.Spell;
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
	
	public int combatTicks = 0;
	
	public Spell delayedSpell = null;
	
	public PseudoPlayerTimer(PseudoPlayer pPlayer) {
		super();
		player = pPlayer;
	}
	
	public PseudoPlayer getPlayer() {
		return player;
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}
	
	public void tick(double delta, long tick) {
		if(player.getOnlinePlayer() == null)
			return;
		if(tick % 10 == 0) { // one second passed 
			updateMana(delta);
			updateStamina(delta);
			bleed();
		}
		if(cantCastTicks > 0)
			cantCastTicks--;
		recentAttackersTick();
		if(spawnTicks > 0)
			spawnTicks--;
		if(player.getCriminal() > 0)
			player.setCriminal(player.getCriminal()-1);
		if(combatTicks > 0)
			combatTicks--;
		if(delayedSpell != null && player.getPromptedSpell() == null)
			delayedSpell.tick(player.getOnlinePlayer());
		if(recentlyTeleportedTicks > 0)
			recentlyTeleportedTicks--;
		spawn();
	}
	
	private void recentAttackersTick() {
		for(RecentAttacker ra : player.getRecentAttackers()) {
			ra.tick();
		}
		player.getRecentAttackers().removeIf(ra ->  ra.isDead());
	}

	private void spawn() {
		if(goToSpawnTicks > 0) {
			goToSpawnTicks--;
			if(goToSpawnTicks == 0) {
				Player player = this.player.getOnlinePlayer();
				player.getWorld().strikeLightningEffect(player.getLocation());
				if(this.player.isCriminal())
					player.teleport(Variables.criminalSpawn);
				else 
					player.teleport(Variables.lawfullSpawn);
				spawnTicks = 36000;
				this.player.setMana(0);
				this.player.setStamina(0);
				player.getWorld().strikeLightningEffect(player.getLocation());
				player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
			}
			else if(goToSpawnTicks % 10 == 0) {
				Player player = this.player.getOnlinePlayer();
				Output.simpleError(player, "Returning to spawn in "+(goToSpawnTicks/10)+" seconds.");
			}
		}
	}

	private void bleed() {
		if(bleedTick > 0) {
			Player p = player.getOnlinePlayer();
			bleedTick--;
			
			if(bleedTick <= 0) {
				p.sendMessage("Your bleeding has stopped.");
				bleedTick = 0;
			}
			else
			{
				double newHealth = p.getHealth() - 1;
				if(newHealth > 20)
					newHealth = 20;
				if(newHealth < 0)
					newHealth = 0;
				p.setHealth(newHealth);
			}
		}
	}

	private void updateMana(double delta) {
		if(player.getMana() < player.getMaxMana()) {
			double manaRegenMultiplier = 2; //Meditation.getManaRegenMultiplier(this);
			if(player.isMeditating())
				player.setMana((int)(player.getMana()+2*manaRegenMultiplier*delta));
			else
				player.setMana((int)(player.getMana()+1*manaRegenMultiplier*delta));
			if(player.getMana() >= player.getMaxMana()) {
				player.setMana(player.getMaxMana());;
				// just reached max...
				Player p = player.getOnlinePlayer();
				if(p != null) {
					Output.positiveMessage(p, "Your mana has fully regenerated.");
				}
			}
		}
	}
	
	private void updateStamina(double delta) {
		if(player.getStamina() < player.getMaxStamina()) {
			double staminaRegenMultiplier = 1;
			if(player.isResting())
				player.setStamina((int)(player.getStamina()+2*staminaRegenMultiplier*delta));
			else
				player.setStamina((int)(player.getStamina()+1*staminaRegenMultiplier*delta));
			if(player.getStamina() >= player.getMaxStamina()) {
				player.setStamina(player.getMaxStamina());;
				// just reached max...
				Player p = player.getOnlinePlayer();
				if(p != null) {
					Output.positiveMessage(p, "Your stamina has fully regenerated.");
				}
			}
		}
	}
	
	public boolean isLastDeathOlder(long ms) {
		return new Date().getTime() > lastDeath+ms;
	}
}
