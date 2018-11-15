package com.lostshard.RPG.CustomContent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class CustomHealingItems {
	public enum FoodType {
		//Apple(260, 4, 10, 4, 2.4f),
		MushroomSoup(282, 10, 15, 8, 9.6f),
		//Bread(297, 5, 13, 5, 6f),
		/*RawPorkchop(319, 3, 20, 3, 1.8f),
		CookedPorkchop(320, 8, 20, 8, 12.8f),*/
		GoldenApple(322, 20, 100, 10, 24f),
		Cookie(357, 2, 5, 1, .2f),
		/*CookedFish(350, 5, 13, 5, 6f),
		RawBeef(363, 3, 20, 3, 1.8f),
		CookedBeef(364, 8, 20, 8, 12.8f),
		RawChicken(365, 3, 13, 3, 1.2f),
		CookedChicken(366, 5, 13, 5, 7.2f),
		RottenFlesh(367, 5, 13, 5, .8f),*/
		Melon(360, 4, 10, 4, 1.2f);
		//RawFish(349, 2, 5, 2, 1.2f);
		
		private final int _itemId;
		private final int _healAmount;
		private final int _staminaCost;
		private final int _fullnessAmount;
		private final float _saturation;
		private static final Map<Integer,FoodType> lookupById  = new HashMap<Integer,FoodType>();
		
		static {
			for(FoodType w : EnumSet.allOf(FoodType.class))
				lookupById.put(w.getId(), w);
	    }
		
		FoodType(int itemId, int healAmount, int staminaCost, int fullnessAmount, float saturation) {
			this._itemId = itemId;
			this._healAmount = healAmount;
			this._staminaCost = staminaCost;
			this._fullnessAmount = fullnessAmount;
			this._saturation = saturation;
		}
		
		public int getId() {
			return _itemId;
		}
		
		public int getHealAmount() {
			return _healAmount;
		}
		
		public int getStaminaCost() {
			return _staminaCost;
		}
		
		public int getFullnessAmount() {
			return _fullnessAmount;
		}
		
		public static FoodType getById(int itemId) {
			if(lookupById.containsKey(itemId))
				return lookupById.get(itemId);
			return null;
		}
		
		public float getSaturation() {
			return _saturation;
		}
	}
}