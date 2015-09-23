package com.lostshard.Lostshard.Factory;

import org.bukkit.entity.EntityType;

import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Utils.RandomSelector;

public class ScrollFactory {
	
	private final RandomSelector zombie;
	private final RandomSelector skeleton;
	private final RandomSelector spider;
	private final RandomSelector creeper;
	private final RandomSelector tier1;
	private final RandomSelector tier2;
	private final RandomSelector tier3;
	private final RandomSelector boss;
	
	
	
	public ScrollFactory() {
		zombie = new RandomSelector();
		zombie.add(1, Scroll.GRASS);
		zombie.add(1, Scroll.FLOWERS);
		zombie.add(1, Scroll.LIGHT);
		zombie.add(1, Scroll.CREATEFOOD);
		
		skeleton = new RandomSelector();
		skeleton.add(1, Scroll.ICEBALL);
		skeleton.add(1, Scroll.FIREFIELD);
		skeleton.add(1, Scroll.HEALSELF);
		skeleton.add(1, Scroll.HEALOTHER);
		skeleton.add(1, Scroll.FIREWALK);
		skeleton.add(1, Scroll.SUMMONANIMAL);
		skeleton.add(1, Scroll.SLOWFIELD);
		skeleton.add(1, Scroll.FORCEPUSH);
		skeleton.add(1, Scroll.CHRONOPORT);
		skeleton.add(1, Scroll.DETECTHIDDEN);
		
		spider = new RandomSelector();
		spider.add(1, Scroll.TELEPORT);
		spider.add(2, Scroll.MARK);
		spider.add(2, Scroll.RECALL);
		spider.add(1, Scroll.BRIDGE);
		spider.add(1, Scroll.WALLOFSTONE);
		
		creeper = new RandomSelector();
		creeper.add(4, Scroll.GATETRAVEL);
		creeper.add(4, Scroll.CLANTELEPORT);
		creeper.add(4, Scroll.FIREBALL);
		creeper.add(4, Scroll.LIGHTNING);
		creeper.add(4, Scroll.STONESKIN);
		creeper.add(1, Scroll.PERMANENTGATETRAVEL);
		creeper.add(4, Scroll.MOONJUMP);
		creeper.add(4, Scroll.ARROWBLAST);
		creeper.add(1, Scroll.RECALL);
		
		tier1 = new RandomSelector();
		for(Scroll s : Scroll.values())
			tier1.add(1, s);
		
		tier2 = new RandomSelector();
		for(Scroll s : Scroll.values())
			tier2.add(1, s);
		
		tier3 = new RandomSelector();
		for(Scroll s : Scroll.values())
			tier3.add(1, s);
		
		boss = new RandomSelector();
		boss.add(5000, Scroll.PERMANENTGATETRAVEL);
		boss.add(5000, Scroll.LIGHTNING);
		boss.add(1, Scroll.FLARE);
	}
	
	public Scroll getRandomScroll(EntityType type) {
		switch (type) {
		case ZOMBIE:
			return (Scroll) zombie.getRandomObject();
		case SKELETON:
			return (Scroll) skeleton.getRandomObject();
		case BLAZE:
			return (Scroll) tier2.getRandomObject();
		case CAVE_SPIDER:
		case SPIDER:
			return (Scroll) spider.getRandomObject();
		case CREEPER:
			return (Scroll) creeper.getRandomObject();
		case GHAST:
			return (Scroll) tier2.getRandomObject();
		case ENDERMAN:
			return (Scroll) tier2.getRandomObject();
		case ENDERMITE:
			return (Scroll) tier1.getRandomObject();
		case GUARDIAN:
			return (Scroll) tier3.getRandomObject();
		case PIG_ZOMBIE:
			return (Scroll) tier2.getRandomObject();
		case WITCH:
			return (Scroll) tier3.getRandomObject();
		case SILVERFISH:
			return (Scroll) tier2.getRandomObject();
		case ENDER_DRAGON:
			return (Scroll) boss.getRandomObject();
		case WITHER:
			return (Scroll) boss.getRandomObject();
		default:
			return null;
		}
	}
	
	public RandomSelector getZombie() {
		return zombie;
	}
}
