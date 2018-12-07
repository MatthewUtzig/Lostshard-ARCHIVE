package com.lostshard.RPG.Skills;

import com.lostshard.RPG.PseudoPlayer;

public class Meditation {
	public static double getManaRegenMultiplier(PseudoPlayer pseudoPlayer) {
		int medSkill = pseudoPlayer.getSkill("meditation");
		double multiplier = 1 + ((double)medSkill/1000);
		return multiplier;
	}
	
	public static int getMaxMana(PseudoPlayer pseudoPlayer) {
		/*int meditationSkill = pseudoPlayer.getSkill("meditation");
		int magerySkill = pseudoPlayer.getSkill("magery");
		
		int medManaBonus = (int)(((double)meditationSkill / 1000)*50);
		int magManaBonus = (int)(((double)magerySkill / 1000)*50);
		
		Player player = Bukkit.getServer().getPlayer(pseudoPlayer.getName());
		if(player == null) {
			return 0;
		}
		
		ItemStack helmet = player.getInventory().getHelmet();
		ItemStack chest = player.getInventory().getChestplate();
		ItemStack leggings = player.getInventory().getLeggings();
		ItemStack boots = player.getInventory().getBoots();
		
		int armorManaPenalty = 0;
		int armorManaBonus = 0;
		
		if(helmet.getType().equals(Material.CHAINMAIL_HELMET)) {
			armorManaPenalty += 6;
		}
		else if(helmet.getType().equals(Material.IRON_HELMET)) {
			armorManaPenalty += 8;
		}
		else if(helmet.getType().equals(Material.DIAMOND_HELMET)) {
			armorManaPenalty += 10;
		}
		else if(helmet.getType().equals(Material.GOLD_HELMET)){
			armorManaBonus += 5;
		}
		
		if(leggings.getType().equals(Material.CHAINMAIL_LEGGINGS)) {
			armorManaPenalty += 6;
		}
		else if(leggings.getType().equals(Material.IRON_LEGGINGS)) {
			armorManaPenalty += 8;
		}
		else if(leggings.getType().equals(Material.DIAMOND_LEGGINGS)) {
			armorManaPenalty += 10;
		}
		else if(leggings.getType().equals(Material.GOLD_LEGGINGS)){
			armorManaBonus += 5;
		}
		
		if(chest.getType().equals(Material.CHAINMAIL_CHESTPLATE)) {
			armorManaPenalty += 6;
		}
		else if(chest.getType().equals(Material.IRON_CHESTPLATE)) {
			armorManaPenalty += 8;
		}
		else if(chest.getType().equals(Material.DIAMOND_CHESTPLATE)) {
			armorManaPenalty += 10;
		}
		else if(chest.getType().equals(Material.GOLD_CHESTPLATE)){
			armorManaBonus += 5;
		}
		
		if(boots.getType().equals(Material.CHAINMAIL_BOOTS)) {
			armorManaPenalty += 6;
		}
		else if(boots.getType().equals(Material.IRON_BOOTS)) {
			armorManaPenalty += 8;
		}
		else if(boots.getType().equals(Material.DIAMOND_BOOTS)) {
			armorManaPenalty += 10;
		}
		else if(boots.getType().equals(Material.GOLD_BOOTS)){
			armorManaBonus += 5;
		}
		
		return 50 + magManaBonus + medManaBonus + armorManaBonus - armorManaPenalty;*/
		
		return 100;// 25 + medManaBonus + magManaBonus; // 25 + up to 50 + up to 50 = max 125
	}
}
