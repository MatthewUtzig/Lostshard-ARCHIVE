package com.lostshard.Crates;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Main.Lostshard;

public class CrateManager {

	private static CrateManager manager = new CrateManager();
	
	private final List<Crate> crates = new ArrayList<Crate>();
	
	private CrateManager() {}
	
	public static CrateManager getManager() {
		return manager;
	}

	public List<Crate> getCrates() {
		return crates;
	}
	
	public Crate getCrate(ItemStack item) {
		for(Crate c : crates)
			if(c.getCrate().isSimilar(item))
				return c;
		return null;
	}
	
	public void createCrates() {
		Crate crate;
		List<CrateItem> loot = new ArrayList<CrateItem>();

		//High value
		loot.add(new CrateItem(0.2, new ItemStack(Material.DIAMOND, 2)));
		loot.add(new CrateItem(0.3, new ItemStack(Material.GOLD_INGOT, 4)));
		loot.add(new CrateItem(0.6, new ItemStack(Material.IRON_INGOT, 6)));
		loot.add(new CrateItem(0.2, new ItemStack(Material.DIAMOND_SWORD, 1)));
		//Low value
		loot.add(new CrateItem(0.8, new ItemStack(Material.DIAMOND, 1)));
		loot.add(new CrateItem(0.8, new ItemStack(Material.GOLD_INGOT, 2)));
		loot.add(new CrateItem(1, new ItemStack(Material.IRON_INGOT, 3)));
		
		crate = new Crate(ChatColor.GOLD+"Vote Crate", new String[] {ChatColor.GOLD+"You can use this key to open the vote crate in Order and Chaos."}, loot);
		crates.add(crate);
		Lostshard.log.warning("Crate chest have been created.");
	}
}
