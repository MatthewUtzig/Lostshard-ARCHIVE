package com.lostshard.Lostshard.Objects;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Utils.Utils;

public class Camp {

	private final double campHealDistance = 5;
	private final UUID creator;
	private int ticksRemaining;
	public boolean isDead = false;
	public Block logBlock;
	public Block fireBlock;

	public Camp(UUID creator, int ticks, Block log, Block fire) {
		this.creator = creator;
		this.ticksRemaining = ticks;
		this.logBlock = log;
		this.fireBlock = fire;
	}

	public void campHealTick() {
		final Location loc = this.logBlock.getLocation();
		for (final Player p : Bukkit.getOnlinePlayers())
			if (Utils.isWithin(p.getLocation(), loc, this.campHealDistance))
				if (p.getHealth() > 0 && p.getHealth() < 20) {
					final double newHealth = Math.min(Math.max(p.getHealth() + 1, 0), 20);
					p.setHealth(newHealth);
				}
	}

	public UUID getCreator() {
		return this.creator;
	}

	public Block getFireBlock() {
		return this.fireBlock;
	}

	public Block getLogBlock() {
		return this.logBlock;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public void tick() {
		if (!this.isDead) {
			boolean doused = false;
			this.ticksRemaining--;
			if (this.ticksRemaining <= 0) {
				final Player creatorPlayer = Bukkit.getPlayer(this.creator);
				if (creatorPlayer != null)
					creatorPlayer.sendMessage(ChatColor.GRAY + "Your camp fire has gone out.");

				this.fireBlock.setType(Material.AIR);
				this.logBlock.setType(Material.AIR);

				this.isDead = true;

			} else if (!this.logBlock.getWorld()
					.getBlockAt(this.logBlock.getX(), this.logBlock.getY(), this.logBlock.getZ()).getType()
					.equals(Material.LOG))
				doused = true;
			else if (!this.fireBlock.getWorld()
					.getBlockAt(this.fireBlock.getX(), this.fireBlock.getY(), this.fireBlock.getZ()).getType()
					.equals(Material.FIRE))
				doused = true;
			if (doused) {
				final Player creatorPlayer = Bukkit.getPlayer(this.creator);
				if (creatorPlayer != null)
					creatorPlayer.sendMessage(ChatColor.GRAY + "Your camp fire has been doused.");
				
				this.fireBlock.setType(Material.AIR);
				this.logBlock.setType(Material.AIR);
				this.isDead = true;
			}
			if (!this.isDead)
				if (this.ticksRemaining % 20 == 0)
					this.campHealTick();
		}
	}

}
