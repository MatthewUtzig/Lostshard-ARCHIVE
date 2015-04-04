package com.lostshard.lostshard.Spells.Spells;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Spells.Spell;

public class SPL_Chronoport extends Spell {
	
	public SPL_Chronoport(Scroll scroll) {
		super(scroll);
	}
	
	int chronoTick = 50;
	private Location startLoc;
	UUID playerUUID;

	@Override
	public boolean verifyCastable(Player player) {
		return true;
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		setStartLoc(player.getLocation());
		pseudoPlayer.getTimer().chronoport = this;
		playerUUID = player.getUniqueId();
	}

	public void chronoTick() {
		if(chronoTick > 0)
			chronoTick--;
		else {
			Player player = Bukkit.getPlayer(playerUUID);
			if(player != null) {
				player.teleport(startLoc);
				PseudoPlayer pPlayer = pm.getPlayer(player);
				pPlayer.getTimer().chronoport = null;
			}
		}
	}

	public Location getStartLoc() {
		return startLoc;
	}

	public void setStartLoc(Location startLoc) {
		this.startLoc = startLoc;
	}

}
