package com.lostshard.lostshard.Factory;

import org.bukkit.entity.EntityType;

import com.lostshard.lostshard.Spells.Scroll;
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
		this.zombie = new RandomSelector();
		this.zombie.add(1, Scroll.GRASS);
		this.zombie.add(1, Scroll.FLOWERS);
		this.zombie.add(1, Scroll.LIGHT);
		this.zombie.add(1, Scroll.CREATEFOOD);

		this.skeleton = new RandomSelector();
		this.skeleton.add(1, Scroll.ICEBALL);
		this.skeleton.add(1, Scroll.FIREFIELD);
		this.skeleton.add(1, Scroll.HEALSELF);
		this.skeleton.add(1, Scroll.HEALOTHER);
		this.skeleton.add(1, Scroll.FIREWALK);
		this.skeleton.add(1, Scroll.SUMMONANIMAL);
		this.skeleton.add(1, Scroll.SLOWFIELD);
		this.skeleton.add(1, Scroll.FORCEPUSH);
		this.skeleton.add(1, Scroll.CHRONOPORT);
		this.skeleton.add(1, Scroll.DETECTHIDDEN);

		this.spider = new RandomSelector();
		this.spider.add(1, Scroll.TELEPORT);
		this.spider.add(2, Scroll.MARK);
		this.spider.add(2, Scroll.RECALL);
		this.spider.add(1, Scroll.BRIDGE);
		this.spider.add(1, Scroll.WALLOFSTONE);

		this.creeper = new RandomSelector();
		this.creeper.add(4, Scroll.GATETRAVEL);
		this.creeper.add(4, Scroll.CLANTELEPORT);
		this.creeper.add(4, Scroll.FIREBALL);
		this.creeper.add(4, Scroll.LIGHTNING);
		this.creeper.add(4, Scroll.STONESKIN);
		this.creeper.add(1, Scroll.PERMANENTGATETRAVEL);
		this.creeper.add(4, Scroll.MOONJUMP);
		this.creeper.add(4, Scroll.ARROWBLAST);
		this.creeper.add(1, Scroll.RECALL);

		this.tier1 = new RandomSelector();
		for (final Scroll s : Scroll.values())
			this.tier1.add(1, s);

		this.tier2 = new RandomSelector();
		for (final Scroll s : Scroll.values())
			this.tier2.add(1, s);

		this.tier3 = new RandomSelector();
		for (final Scroll s : Scroll.values())
			this.tier3.add(1, s);

		this.boss = new RandomSelector();
		this.boss.add(5000, Scroll.PERMANENTGATETRAVEL);
		this.boss.add(5000, Scroll.LIGHTNING);
		this.boss.add(1, Scroll.FLARE);
	}

	public Scroll getRandomScroll(EntityType type) {
		switch (type) {
		case ZOMBIE:
			return (Scroll) this.zombie.getRandomObject();
		case SKELETON:
			return (Scroll) this.skeleton.getRandomObject();
		case BLAZE:
			return (Scroll) this.tier2.getRandomObject();
		case CAVE_SPIDER:
		case SPIDER:
			return (Scroll) this.spider.getRandomObject();
		case CREEPER:
			return (Scroll) this.creeper.getRandomObject();
		case GHAST:
			return (Scroll) this.tier2.getRandomObject();
		case ENDERMAN:
			return (Scroll) this.tier2.getRandomObject();
		case ENDERMITE:
			return (Scroll) this.tier1.getRandomObject();
		case GUARDIAN:
			return (Scroll) this.tier3.getRandomObject();
		case PIG_ZOMBIE:
			return (Scroll) this.tier2.getRandomObject();
		case WITCH:
			return (Scroll) this.tier3.getRandomObject();
		case SILVERFISH:
			return (Scroll) this.tier2.getRandomObject();
		case ENDER_DRAGON:
			return (Scroll) this.boss.getRandomObject();
		case WITHER:
			return (Scroll) this.boss.getRandomObject();
		default:
			return null;
		}
	}

	public RandomSelector getZombie() {
		return this.zombie;
	}
}
