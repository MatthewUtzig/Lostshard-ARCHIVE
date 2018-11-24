package com.lostshard.RPG.CustomContent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class CustomWeapons {
	public enum WeaponType {
		WoodenSword(268, "wood", 200, 60),
		StoneSword(272, "stone", 300, 132),
		IronSword(267, "iron", 400, 251),
		DiamondSword(276, "diamond", 500, 1562),
		GoldSword(283, "gold", 200, 33);
		
		private final int itemId;
		private final String material;
		private final int baseDamage;
		private final int durability;
		private static final Map<Integer,WeaponType> lookupById  = new HashMap<Integer,WeaponType>();
		
		static {
			for(WeaponType w : EnumSet.allOf(WeaponType.class))
				lookupById.put(w.getId(), w);
	    }
		
		WeaponType(int itemId, String material, int baseDamage, int durability) {
			this.itemId = itemId;
			this.material = material;
			this.baseDamage = baseDamage;
			this.durability = durability;
		}
		
		public int getId() {
			return itemId;
		}
		
		public String getMaterial() {
			return material;
		}
		
		public int getBaseDamage() {
			return baseDamage;
		}
		
		public int getDurability() {
			return durability;
		}
		
		public static WeaponType getById(int itemId) {
			if(lookupById.containsKey(itemId))
				return lookupById.get(itemId);
			return null;
		}
	}
}
