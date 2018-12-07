package com.lostshard.RPG.CustomContent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class CustomArmor {
	public enum ArmorType {
		LeatherHelmet(298, "leather", 4, 34),
		LeatherChestplate(299, "leather", 6, 48),
		LeatherLeggings(300, "leather", 6, 46),
		LeatherBoots(301, "leather", 4, 40),
		ChainmailHelmet(302, "chainmail", 5, 67),
		ChainmailChestplate(303, "chainmail", 10, 96),
		ChainmailLeggings(304, "chainmail", 10, 92),
		ChainmailBoots(305, "chainmail", 5, 79),
		IronHelmet(306, "iron", 8, 136),
		IronChestplate(307, "iron", 12, 192),
		IronLeggings(308, "iron", 12, 184),
		IronBoots(309, "iron", 8, 160),
		DiamondHelmet(310, "diamond", 10, 272),
		DiamondChestplate(311, "diamond", 20, 384),
		DiamondLeggings(312, "diamond", 20, 368),
		DiamondBoots(313, "diamond", 10, 320),
		GoldHelmet(314, "gold", 4, 68),
		GoldChestplate(315, "gold", 6, 96),
		GoldLeggings(316, "gold", 6, 92),
		GoldBoots(317, "gold", 4, 80);
		
		private final int itemId;
		private final String material;
		private final int baseDefense;
		private final int durability;
		private static final Map<Integer,ArmorType> lookupById  = new HashMap<Integer,ArmorType>();
		
		static {
			for(ArmorType w : EnumSet.allOf(ArmorType.class))
				lookupById.put(w.getId(), w);
	    }
		
		ArmorType(int itemId, String material, int baseDefense, int durability) {
			this.itemId = itemId;
			this.material = material;
			this.baseDefense = baseDefense;
			this.durability = durability;
		}
		
		public int getId() {
			return itemId;
		}
		
		public String getMaterial() {
			return material;
		}
		
		public int getBaseDefense() {
			return baseDefense;
		}
		
		public int getDurability() {
			return durability;
		}
		
		public static ArmorType getById(int itemId) {
			if(lookupById.containsKey(itemId))
				return lookupById.get(itemId);
			return null;
		}
	}
}
