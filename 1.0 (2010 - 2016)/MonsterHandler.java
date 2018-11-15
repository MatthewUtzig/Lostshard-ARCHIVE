package com.lostshard.RPG;

import com.lostshard.RPG.Spells.*;

import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Slime;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton;

public class MonsterHandler {
	
	public static Spell getDroppedSpell(Monster monster) {
		if(monster instanceof Giant) {
			int numSpells = 29;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
				return new SPL_Grass();
			case 1:
				return new SPL_Flowers();
			case 2:
				return new SPL_Light();
			case 3:
				return new SPL_CreateFood();
			case 4:
				return new SPL_MagicArrow();
			case 5:
				return new SPL_MagicArrow();
			case 6:
				return new SPL_Teleport();
			case 7:
				return new SPL_Recall();
			case 8:
				return new SPL_Mark();
			case 9:
				return new SPL_Bridge();
			case 10:
				return new SPL_Iceball();
			case 11:
				return new SPL_Firefield();
			case 12:
				return new SPL_Heal();
			case 13:
				return new SPL_HealSelf();
			case 14:
				return new SPL_FireWalk();
			case 15:
				return new SPL_GateTravel();
			case 16:
				return new SPL_ClanTeleport();
			case 17:
				return new SPL_PermanentGateTravel();
			/*case 18:
				return new SPL_Fly();*/
			case 18:
				return new SPL_Lightning();
			case 19:
				return new SPL_Day();
			case 20:
				return new SPL_MoonJump();
			case 21:
				return new SPL_SummonAnimal();
			case 22:
				return new SPL_SummonMonster();
			case 23:
				return new SPL_Fireball();
			case 24:
				return new SPL_ClearSky();
			case 25:
				return new SPL_StoneSkin();
			case 26:
				return new SPL_SlowField();
			case 27:
				return new SPL_WallOfStone();
			case 28:
				return new SPL_ForcePush();
			}
		}
		if(monster instanceof Zombie && !(monster instanceof PigZombie)) {
			int numSpells = 4;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
				return new SPL_Grass();
			case 1:
				return new SPL_Flowers();
			case 2:
				return new SPL_Light();
			case 3:
				return new SPL_CreateFood();
			}
		}
		else if(monster instanceof Spider) {
			int numSpells = 6;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
				return new SPL_MagicArrow();
			case 1:
				return new SPL_Teleport();
			case 2:
				return new SPL_Recall();
			case 3:
				return new SPL_Mark();
			case 4:
				return new SPL_Bridge();
			case 5:
				return new SPL_WallOfStone();
			}
		}
		else if(monster instanceof Skeleton) {
			int numSpells = 10;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
				return new SPL_Iceball();
			case 1:
				return new SPL_Firefield();
			case 2:
				return new SPL_Heal();
			case 3:
				return new SPL_HealSelf();
			case 4:
				return new SPL_FireWalk();
			case 5:
				return new SPL_SummonAnimal();
			case 6:
				return new SPL_SlowField();
			case 7:
				return new SPL_ForcePush();
			case 8:
				return new SPL_Chronoport();
			case 9:
				return new SPL_DetectHidden();
			}
		}
		else if(monster instanceof Creeper) {
			int numSpells = 26;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
			case 1:
			case 2:
			case 3:
				return new SPL_GateTravel();
			case 5:
			case 6:
			case 7:
			case 8:
				return new SPL_ClanTeleport();
			case 9:
			case 10:
			case 11:
			case 12:
				return new SPL_Fireball();
			case 13:
			case 14:
			case 15:
			case 16:
				return new SPL_Lightning();
			case 17:
			case 18:
			case 19:
			case 20:
				return new SPL_StoneSkin();
			case 21:
				return new SPL_PermanentGateTravel();
			case 22:
				return new SPL_Day();
			case 23:
				return new SPL_ClearSky();
			case 24:
				return new SPL_SummonMonster();
			case 25:
				return new SPL_MoonJump();
			}
		}
		else if(monster instanceof Ghast) {
			int numSpells = 5;
			int randInt = (int)Math.floor(Math.random()*numSpells);
			switch(randInt) {
			case 0:
				return new SPL_PermanentGateTravel();
			case 1:
				return new SPL_Day();
			case 2:
				return new SPL_ClearSky();
			case 3:
				return new SPL_MoonJump();
			case 4:
				return new SPL_SummonMonster();
			}
		}
		
		int numSpells = 29;
		int randInt = (int)Math.floor(Math.random()*numSpells);
		switch(randInt) {
		case 0:
			return new SPL_Grass();
		case 1:
			return new SPL_Flowers();
		case 2:
			return new SPL_Light();
		case 3:
			return new SPL_CreateFood();
		case 4:
			return new SPL_MagicArrow();
		case 5:
			return new SPL_MagicArrow();
		case 6:
			return new SPL_Teleport();
		case 7:
			return new SPL_Recall();
		case 8:
			return new SPL_Mark();
		case 9:
			return new SPL_Bridge();
		case 10:
			return new SPL_Iceball();
		case 11:
			return new SPL_Firefield();
		case 12:
			return new SPL_Heal();
		case 13:
			return new SPL_HealSelf();
		case 14:
			return new SPL_FireWalk();
		case 15:
			return new SPL_GateTravel();
		case 16:
			return new SPL_ClanTeleport();
		case 17:
			return new SPL_PermanentGateTravel();
		/*case 18:
			return new SPL_Fly();*/
		case 18:
			return new SPL_Lightning();
		case 19:
			return new SPL_Day();
		case 20:
			return new SPL_Fireball();
		case 21:
			return new SPL_ClearSky();
		case 22:
			return new SPL_MoonJump();
		case 23:
			return new SPL_SummonAnimal();
		case 24:
			return new SPL_SummonMonster();
		case 25:
			return new SPL_StoneSkin();
		case 26:
			return new SPL_SlowField();
		case 27:
			return new SPL_WallOfStone();
		case 28:
			return new SPL_ForcePush();
		default:
			return new SPL_Grass();
		}
	}
	
	public static String getMonsterName(Entity entity) {
		if(entity instanceof Creeper)
			return "Creeper";
		else if(entity instanceof Ghast)
			return "Ghast";
		else if(entity instanceof Skeleton)
			return "Skeleton";
		else if(entity instanceof Slime)
			return "Slime";
		else if(entity instanceof Spider)
			return "Spider";
		else if(entity instanceof Zombie)
			return "Zombie";
		else if(entity instanceof Giant)
			return "Giant";
		else if(entity instanceof Enderman)
			return "Enderman";
		else if(entity instanceof Blaze)
			return "Blaze";
		else if(entity instanceof EnderDragon)
			return "Ender Dragon";
		return "a";
	}
}
