package com.lostshard.RPG.CustomContent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class CustomTools {
	public enum ToolType {
		WoodenSword(268, "wood", 60, true, false, false, false),
		StoneSword(272, "stone", 132, true, false, false, false),
		IronSword(267, "iron", 251, true, false, false, false),
		DiamondSword(276, "diamond", 1562, true, false, false, false),
		GoldSword(283, "gold", 33, true, false, false, false),
		
		WoodenShovel(269, "wood", 60, false, true, false, true),
		StoneShovel(273, "stone", 132, false, true, false, true),
		IronShovel(256, "iron", 251, false, true, false, true),
		DiamondShovel(277, "diamond", 1562, false, true, false, true),
		GoldShovel(284, "gold", 33, false, true, false, true),
		
		WoodenPick(270, "wood", 60, false, true, false, true),
		StonePick(274, "stone", 132, false, true, false, true),
		IronPick(257, "iron", 251, false, true, false, true),
		DiamondPick(278, "diamond", 1562, false, true, false, true),
		GoldPick(285, "gold", 33, false, true, false, true),
		
		WoodenAxe(271 , "wood", 60, true, true, false, false),
		StoneAxe(275, "stone", 132, true, true, false, false),
		IronAxe(258, "iron", 251, true, true, false, false),
		DiamondAxe(279, "diamond", 1562, true, true, false, false),
		GoldAxe(286, "gold", 33, true, true, false, false),
		
		WoodenHoe(290, "wood", 60, false, false, false, false),
		StoneHoe(291, "stone", 132, false, false, false, false),
		IronHoe(292, "iron", 251, false, false, false, false),
		DiamondHoe(293, "diamond", 1562, false, false, false, false),
		GoldHoe(294, "gold", 33, false, false, false, false),
		
		Bow(261, "string", 385, false, false, true, false);
		
		private final int itemId;
		private final String material;
		private final int durability;
		public final boolean canSharpen;
		public final boolean canFortify;
		public final boolean canPower;
		public final boolean canEnhance;
		private static final Map<Integer,ToolType> lookupById  = new HashMap<Integer,ToolType>();
		
		static {
			for(ToolType w : EnumSet.allOf(ToolType.class))
				lookupById.put(w.getId(), w);
	    }
		
		ToolType(int itemId, String material, int durability, boolean canSharpen, boolean canFortify, boolean canPower, boolean canEnhance) {
			this.itemId = itemId;
			this.material = material;
			this.durability = durability;
			this.canSharpen = canSharpen;
			this.canFortify = canFortify;
			this.canPower = canPower;
			this.canEnhance = canEnhance;
		}
		
		public int getId() {
			return itemId;
		}
		
		public String getMaterial() {
			return material;
		}
		
		public int getDurability() {
			return durability;
		}
		
		public static ToolType getById(int itemId) {
			if(lookupById.containsKey(itemId))
				return lookupById.get(itemId);
			return null;
		}
	}
}
