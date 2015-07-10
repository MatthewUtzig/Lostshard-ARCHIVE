package com.lostshard.Lostshard.Spells.Spells;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Spells.Spell;

public class SPL_Chronoport extends Spell {

	int chronoTick = 50;

	private Location startLoc;
	UUID playerUUID;

	public SPL_Chronoport(Scroll scroll) {
		super(scroll);
	}

	public void chronoTick() {
		if (this.chronoTick > 0)
			this.chronoTick--;
		else {
			final Player player = Bukkit.getPlayer(this.playerUUID);
			if (player != null) {
				player.teleport(this.startLoc);
				final PseudoPlayer pPlayer = this.pm.getPlayer(player);
				pPlayer.getTimer().chronoport = null;
			}
		}
	}

	@Override
	public void doAction(Player player) {
		final PseudoPlayer pseudoPlayer = this.pm.getPlayer(player);
		this.setStartLoc(player.getLocation());
		pseudoPlayer.getTimer().chronoport = this;
		this.playerUUID = player.getUniqueId();
	}

	public Location getStartLoc() {
		return this.startLoc;
	}

	@Override
	public void preAction(Player player) {

	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

}
