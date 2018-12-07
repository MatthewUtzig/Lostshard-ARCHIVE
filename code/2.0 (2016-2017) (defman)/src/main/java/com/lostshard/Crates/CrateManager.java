package com.lostshard.Crates;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Utils.RandomSelector;

import net.md_5.bungee.api.ChatColor;

public class CrateManager {

	private static CrateManager manager = new CrateManager();

	public static CrateManager getManager() {
		return manager;
	}

	private final List<Crate> crates = new ArrayList<Crate>();

	private CrateManager() {
	}

	public void createCrates() {
		Crate crate;
		RandomSelector rs;

		rs = new RandomSelector();

		// High value
		rs.add(5, new ItemStack(Material.DIAMOND, 3));
		rs.add(5, new ItemStack(Material.GOLD_INGOT, 5));
		rs.add(5, new ItemStack(Material.IRON_INGOT, 7));
		rs.add(1, new ItemStack(Material.DIAMOND_SWORD, 1));
		rs.add(1, new ItemStack(Material.MONSTER_EGG, 1, (short) 96));
		// Low value
		rs.add(1, new ItemStack(Material.DIAMOND, 2));
		rs.add(1, new ItemStack(Material.GOLD_INGOT, 3));
		rs.add(1, new ItemStack(Material.IRON_INGOT, 4));

		crate = new Crate(2, ChatColor.GOLD + "Legendary Crate",
				new String[] { ChatColor.GOLD + "Crate's contains random loot." }, rs);
		this.crates.add(crate);

		rs = new RandomSelector();

		// High value
		rs.add(80, new ItemStack(Material.DIAMOND, 2));
		rs.add(80, new ItemStack(Material.GOLD_INGOT, 4));
		rs.add(120, new ItemStack(Material.IRON_INGOT, 6));
		rs.add(20, new ItemStack(Material.DIAMOND_SWORD, 1));
		rs.add(1, new ItemStack(Material.MONSTER_EGG, 1, (short) 96));
		rs.add(1, this.crates.get(0).getCrate());
		// Low value
		rs.add(1, new ItemStack(Material.DIAMOND, 1));
		rs.add(1, new ItemStack(Material.GOLD_INGOT, 2));
		rs.add(300, new ItemStack(Material.IRON_INGOT, 3));

		crate = new Crate(1, ChatColor.GOLD + "Vote Crate",
				new String[] { ChatColor.GOLD + "Crate's contains random loot." }, rs);
		this.crates.add(crate);

		Lostshard.log.warning("Crate chest have been created.");
	}

	public Crate getCrate(ItemStack item) {
		for (final Crate c : this.crates)
			if (item.hasItemMeta() && item.getItemMeta().hasEnchants()
					&& item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ENCHANTS)
					&& item.getItemMeta().getItemFlags().contains(ItemFlag.HIDE_ATTRIBUTES)
					&& item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore()
					&& item.getItemMeta().getEnchantLevel(Enchantment.DURABILITY) == c.getId())
				return c;
		return null;
	}

	public List<Crate> getCrates() {
		return this.crates;
	}
	
	public Crate getCrateByName(String name) {
		for(Crate c : getCrates())
			if(StringUtils.startsWithIgnoreCase(ChatColor.stripColor(c.getName()), name))
				return c;	
		return null;
	}
}
