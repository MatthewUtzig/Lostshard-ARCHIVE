package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.PVPHandler;
import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Utils.Utils;

public class SPL_Lightning extends RangedSpell {

	public SPL_Lightning() {
		setName("Lightning");
		setSpellWords("Zeusius Similaricus");
		setCastingDelay(0);
		setCarePlot(false);
		setPage(7);
		setCooldown(20);
		setManaCost(15);
		addReagentCost(new ItemStack(Material.STRING));
		addReagentCost(new ItemStack(Material.REDSTONE));
		setMinMagery(720);
		setRange(20);
	}

	@Override
	public void preAction(Player player) {
		
	}

	@Override
	public void doAction(Player player) {
		Location strikeLoc = getFoundBlock().getLocation();
		strikeLoc.setX(strikeLoc.getX()+.5);
		strikeLoc.setZ(strikeLoc.getZ()+.5);
		getFoundBlock().getWorld().strikeLightning(strikeLoc);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(Utils.isWithin(p.getLocation(), strikeLoc, 5)) {
				//Casting lightning at someone?
				if(PVPHandler.canEntityAttackEntity(player, p)) {
					PVPHandler.criminalAction(p, player);
				}				
			}
		}

	}

}
