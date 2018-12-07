package com.lostshard.RPG;

import java.util.ArrayList;
import java.util.Random;

import javax.tools.JavaFileManager.Location;

import org.bukkit.entity.CreatureType;
import org.bukkit.inventory.ItemStack;

public class TreasureMap {
	private final short _mapId;
	private final int _level;
	private final Location _treasureLocation;
	private static final int _minLevel0Items = 5;
	private static final int _maxLevel0Items = 15;
	public static final int[] _level0LootTable = {
		268,269,270,271,281,280,287,289,295,297,266,265,264,290,296,298,299,300,301,306,307,328,332
	};
	
	private static final int _minLevel1Items = 5;
	private static final int _maxLevel1Items = 15;
	public static final int[] _level1LootTable = {
		272,273,274,275,302,303,304,305,291,336,337,338,339,266,265,264
	};
	
	private static final int _minLevel2Items = 5;
	private static final int _maxLevel2Items = 15;
	public static final int[] _level2LootTable = {
		264,266,306,307,308,309,256,257,258,267,292,332,38,37,329
	};
	
	private static final int _minLevel3Items = 5;
	private static final int _maxLevel3Items = 15;
	public static final int[] _level3LootTable = {
		264,266,264,266,264,266,310,311,312,313,276,277,278,279,293,57,30,2256
	};
	
	private static final int _minLevel4Items = 5;
	private static final int _maxLevel4Items = 15;
	public static final int[] _level4LootTable = {
		264,266,314,315,316,317,368,41,57,3,283,284,285,286,294,322,2256,2257,122
	};
	
	public TreasureMap(short mapId, int level, Location treasureLocation) {
		_mapId = mapId;
		_level = level;
		_treasureLocation = treasureLocation;
	}
	
	public short getMapId() {
		return _mapId;
	}
	
	public int getLevel() {
		return _level;
	}
	
	public Location getLocation() {
		return _treasureLocation;
	}
	
	public CreatureType getRandMob() {
		Random random = new Random();
		
		if(_level == 0) {
			int randInt = random.nextInt(2);
			if(randInt == 0)
				return CreatureType.ZOMBIE;
			else if(randInt == 1)
				return CreatureType.SKELETON;
		}
		else if(_level == 1) {
			int randInt = random.nextInt(4);
			if(randInt == 0)
				return CreatureType.ZOMBIE;
			else if(randInt == 1)
				return CreatureType.SKELETON;
			else if(randInt == 2)
				return CreatureType.SPIDER;
			else if(randInt == 3)
				return CreatureType.SLIME;
		}
		else if(_level == 2) {
			int randInt = random.nextInt(7);
			if(randInt == 0)
				return CreatureType.ZOMBIE;
			else if(randInt == 1)
				return CreatureType.SKELETON;
			else if(randInt == 2)
				return CreatureType.SPIDER;
			else if(randInt == 3)
				return CreatureType.SLIME;
			else if(randInt == 4)
				return CreatureType.CREEPER;
			else if(randInt == 5)
				return CreatureType.CAVE_SPIDER;
			else if(randInt == 6)
				return CreatureType.SILVERFISH;
		}
		else if(_level == 3) {
			int randInt = random.nextInt(5);
			if(randInt == 0)
				return CreatureType.SLIME;
			else if(randInt == 1)
				return CreatureType.CREEPER;
			else if(randInt == 2)
				return CreatureType.CAVE_SPIDER;
			else if(randInt == 3)
				return CreatureType.SILVERFISH;
			else if(randInt == 4)
				return CreatureType.GHAST;
		}
		else if(_level == 4) {
			int randInt = random.nextInt(6);
			if(randInt == 0)
				return CreatureType.CREEPER;
			else if(randInt == 1)
				return CreatureType.CAVE_SPIDER;
			else if(randInt == 2)
				return CreatureType.SILVERFISH;
			else if(randInt == 3)
				return CreatureType.GHAST;
			else if(randInt == 4)
				return CreatureType.GIANT;
			else if(randInt == 5)
				return CreatureType.ENDERMAN;
		}
		
		return null;
	}
	
	public ArrayList<ItemStack> getRandLoot() {
		ArrayList<ItemStack> randLoot = new ArrayList<ItemStack>();
		
		if(_level == 0) {
			return getRandLootLevelled(randLoot, _minLevel0Items, _maxLevel0Items, _level0LootTable);
		}
		else if(_level == 1) {
			return getRandLootLevelled(randLoot, _minLevel1Items, _maxLevel1Items, _level1LootTable);
		}
		else if(_level == 2) {
			return getRandLootLevelled(randLoot, _minLevel2Items, _maxLevel2Items, _level2LootTable);
		}
		else if(_level == 3) {
			return getRandLootLevelled(randLoot, _minLevel3Items, _maxLevel3Items, _level3LootTable);
		}
		else if(_level == 4) {
			return getRandLootLevelled(randLoot, _minLevel4Items, _maxLevel4Items, _level4LootTable);
		}
		
		return randLoot;
	}
	
	private ArrayList<ItemStack> getRandLootLevelled(ArrayList<ItemStack> randLoot, int minItems, int maxItems, int[] lootTable) {
		Random random = new Random();
		int numItems = random.nextInt(maxItems-minItems)+_minLevel0Items;
		int numItemsInLootTable = lootTable.length;
		for(int i=0; i<numItems; i++) {
			int randItemId = lootTable[random.nextInt(numItemsInLootTable)];
			randLoot.add(new ItemStack(randItemId, 1));
		}
		
		return randLoot;
	}
}
