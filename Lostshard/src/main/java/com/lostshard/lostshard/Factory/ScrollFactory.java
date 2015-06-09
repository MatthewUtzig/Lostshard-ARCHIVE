package com.lostshard.lostshard.Factory;

import java.util.Random;

import org.bukkit.entity.EntityType;

import com.lostshard.lostshard.Spells.Scroll;

public class ScrollFactory {

	public Scroll getRandomScroll(EntityType type) {
		final Random rand = new Random();
		switch (type) {
		case ZOMBIE:
			switch (rand.nextInt(4)) {
			case 0:
				return Scroll.GRASS;
			case 1:
				return Scroll.FLOWERS;
			case 2:
				return Scroll.LIGHT;
			default:
				return Scroll.CREATEFOOD;
			}
		case SKELETON:
			switch (rand.nextInt(10)) {
			case 0:
				return Scroll.ICEBALL;
			case 1:
				return Scroll.FIREFIELD;
			case 2:
				return Scroll.HEALSELF;
			case 3:
				return Scroll.HEALOTHER;
			case 4:
				return Scroll.FIREWALK;
			case 5:
				return Scroll.SUMMONANIMAL;
			case 6:
				return Scroll.SLOWFIELD;
			case 7:
				return Scroll.FORCEPUSH;
			case 8:
				return Scroll.CHRONOPORT;
			default:
				return Scroll.DETECTHIDDEN;
			}
		case BLAZE:
			return null;
		case CAVE_SPIDER:
		case SPIDER:
			switch (rand.nextInt(5)) {
			case 0:
				return Scroll.TELEPORT;
			case 1:
			case 2:
				return Scroll.MARK;
			case 3:
				return Scroll.BRIDGE;
			default:
				return Scroll.WALLOFSTONE;
			}
		case CREEPER:
			switch (rand.nextInt(27)) {
			case 0:
			case 1:
			case 2:
			case 3:
				return Scroll.GATETRAVEL;
			case 5:
			case 6:
			case 7:
			case 8:
				return Scroll.CLANTELEPORT;
			case 9:
			case 10:
			case 11:
			case 12:
				return Scroll.FIREBALL;
			case 13:
			case 14:
			case 15:
			case 16:
				return Scroll.LIGHTNING;
			case 17:
			case 18:
			case 19:
			case 20:
				return Scroll.STONESKIN;
			case 21:
				return Scroll.PERMANENTGATETRAVEL;
			case 22:
				// return Scroll.DAY;
				return null;
			case 23:
				// return Scroll.CLEARSKY;
				return null;
			case 24:
				return Scroll.SUMMONMONSTER;
			case 25:
				return Scroll.MOONJUMP;
			default:
				return Scroll.ARROWBLAST;
			}
		case GHAST:
			switch (rand.nextInt(6)) {
			case 0:
				return Scroll.PERMANENTGATETRAVEL;
			case 1:
				// return Scroll.DAY;
				return null;
			case 2:
				// return Scroll.CLEARSKY;
				return null;
			case 3:
				return Scroll.MOONJUMP;
			case 4:
				return Scroll.SUMMONMONSTER;
			default:
				return Scroll.ARROWBLAST;
			}
		case ENDERMAN:
			return null;
		case ENDERMITE:
			return null;
		case GUARDIAN:
			return null;
		case PIG_ZOMBIE:
			return null;
		case WITCH:
			return null;
		case SILVERFISH:
			return null;
		case ENDER_DRAGON:
			return null;
		case WITHER:
			return null;
		default:
			return null;
		}
	}
}
