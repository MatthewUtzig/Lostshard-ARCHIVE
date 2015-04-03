package com.lostshard.lostshard.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Spells.Spells.SPL_ArrowBlast;
import com.lostshard.lostshard.Spells.Spells.SPL_Bridge;
import com.lostshard.lostshard.Spells.Spells.SPL_Chronoport;
import com.lostshard.lostshard.Spells.Spells.SPL_ClanTeleport;
import com.lostshard.lostshard.Spells.Spells.SPL_CreateFood;
import com.lostshard.lostshard.Spells.Spells.SPL_DetectHidden;
import com.lostshard.lostshard.Spells.Spells.SPL_FireWalk;
import com.lostshard.lostshard.Spells.Spells.SPL_Fireball;
import com.lostshard.lostshard.Spells.Spells.SPL_Firefield;
import com.lostshard.lostshard.Spells.Spells.SPL_Flare;
import com.lostshard.lostshard.Spells.Spells.SPL_Flowers;
import com.lostshard.lostshard.Spells.Spells.SPL_Forcepush;
import com.lostshard.lostshard.Spells.Spells.SPL_GateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Grass;
import com.lostshard.lostshard.Spells.Spells.SPL_HealOther;
import com.lostshard.lostshard.Spells.Spells.SPL_HealSelf;
import com.lostshard.lostshard.Spells.Spells.SPL_Iceball;
import com.lostshard.lostshard.Spells.Spells.SPL_Light;
import com.lostshard.lostshard.Spells.Spells.SPL_Lightning;
import com.lostshard.lostshard.Spells.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.Spells.SPL_MoonJump;
import com.lostshard.lostshard.Spells.Spells.SPL_PermanentGateTravel;
import com.lostshard.lostshard.Spells.Spells.SPL_Recall;
import com.lostshard.lostshard.Spells.Spells.SPL_Slowfield;
import com.lostshard.lostshard.Spells.Spells.SPL_StoneSkin;
import com.lostshard.lostshard.Spells.Spells.SPL_SummonAnimal;
import com.lostshard.lostshard.Spells.Spells.SPL_Teleport;
import com.lostshard.lostshard.Spells.Spells.SPL_WallOfStone;

public enum Scroll {
	MARK("Mark","Runus Markius",4,10,20,20,360, new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	TELEPORT("Teleport","Nearius Porticus",3,20,20,0,240,new ItemStack[] {new ItemStack(Material.FEATHER)}), 
	RECALL("Recall","Runus Teleporticus",4,30,20,20,360,new ItemStack[] {new ItemStack(Material.FEATHER)}), 
	PERMANENTGATETRAVEL("Permanent Gate Travel","Gatius Permenatus",8,100,20,20,840,new ItemStack[] {new ItemStack(Material.STRING), new ItemStack(Material.REDSTONE), new ItemStack(Material.OBSIDIAN)}),
	GATETRAVEL("Gate Travel","Gatius Teleportus",7,50,20,20,720,new ItemStack[] {new ItemStack(Material.STRING), new ItemStack(Material.REDSTONE)}),
	FLARE("Flare","Beforius Flarius",1,0,0,10,0,new ItemStack[] {new ItemStack(Material.SULPHUR)}),
	SLOWFIELD("Slow Field","Webicus Fieldicus",5,15,20,0,480,new ItemStack[] {new ItemStack(Material.STRING)}),
	GRASS("Grass","Grassius Maximus",1,5,20,0,0,new ItemStack[] {new ItemStack(Material.SEEDS)}),
	ICEBALL("Ice Ball","Freezius Ballicus",5,25,20,0,480,new ItemStack[] {new ItemStack(Material.STRING)}),
	ARROWBLAST("Arrow Blast","Blastius Projectilus",3,20,20,0,240,new ItemStack[] {new ItemStack(Material.ARROW), new ItemStack(Material.SULPHUR)}),
	CLANTELEPORT("Clan Teleport","Arg Matius Teleportus",7,50,20,20,720,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	LIGHT("Light","Lightus Flingicus",2,10,20,0,120,new ItemStack[] {new ItemStack(Material.SUGAR_CANE)}),
	HEALSELF("Heal Self","Selfishius Healicus",6,35,20,0,600,new ItemStack[] {new ItemStack(Material.STRING), new ItemStack(Material.REDSTONE)}),
	HEALOTHER("Heal Other","Buddius Healicus",6,20,20,0,600,new ItemStack[] {new ItemStack(Material.STRING), new ItemStack(Material.SEEDS)}),
	LIGHTNING("Lightning","Zeusius Similaricus",7,15,20,0,720,new ItemStack[] {new ItemStack(Material.STRING), new ItemStack(Material.REDSTONE)}),
	FIREFIELD("Fire Field","Charmanderous Fieldicus",5,20,20,0,480,new ItemStack[] {new ItemStack(Material.SULPHUR)}),
	SUMMONANIMAL("Summon Animal","Magickus Bambicus",5,40,20,0,480,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	SUMMONMONSTER("Summon Monster","Magickus Tradjicus",8,60,20,0,840,new ItemStack[] {new ItemStack(Material.BONE), new ItemStack(Material.REDSTONE)}),
	BRIDGE("Bridge","An Bridgius",4,20,10,0,360,new ItemStack[] {new ItemStack(Material.SUGAR_CANE)}),
	FLOWERS("Flowers","Flowerus Erupticus",1,5,20,0,0,new ItemStack[] {new ItemStack(Material.SEEDS)}),
	FIREBALL("Fireball","Charmanderous Ballicus",7,10,5,0,720,new ItemStack[] {new ItemStack(Material.SULPHUR)}),
	FIREWALK("Fire Walk","Charmanderous Feetius",6,25,20,0,600,new ItemStack[] {new ItemStack(Material.SULPHUR), new ItemStack(Material.REDSTONE)}),
	CREATEFOOD("Createfood","Magickus Delicious",2,10,5,0,120,new ItemStack[] {new ItemStack(Material.SEEDS)}),
	DETECTHIDDEN("Detect Hidden","Sneakthiefius Discoverus",5,50,20,0,480,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	STONESKIN("Stone Skin","Rockius Polymorhpus",7,50,20,0,720,new ItemStack[] {new ItemStack(Material.STONE), new ItemStack(Material.REDSTONE)}),
	MOONJUMP("Moon Jump","Hypnoticus Astronauticus",8,30,20,0,840,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	CHRONOPORT("Chronoport","Rubberus Bandius",5,25,20,0,480,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	FORCEPUSH("Force Push","Fus Ro Dah!",6,15,20,0,600,new ItemStack[] {new ItemStack(Material.FEATHER), new ItemStack(Material.REDSTONE)}),
	WALLOFSTONE("Wall of Stone","Blockus Rockius",4,15,20,0,360,new ItemStack[] {new ItemStack(Material.STONE), new ItemStack(Material.REDSTONE)});	
	
	private String name;
	private String spellWords;
	private int manaCost;
	private int minMagery;
	private int page;
	private int cooldown;
	private int castingDelay;
	private List<ItemStack> reagentCost = new ArrayList<ItemStack>();
	
	private Scroll(String name, String spellWords, int page, int manaCost,
			int cooldown, int castingDelay, int minMagery, ItemStack[] reagentCost) {
		this.name = name;
		this.spellWords = spellWords;
		this.page = page;
		this.manaCost = manaCost;
		this.minMagery = minMagery;
		this.cooldown = cooldown;
		this.castingDelay = castingDelay;
		for(ItemStack item : reagentCost)
			this.reagentCost.add(item);
	}
	
	public Spell getSpell() {
		switch (this) {
			case MARK:
				return new SPL_Mark(this);
			case TELEPORT:
				return new SPL_Teleport(this);
			case RECALL:
				return new SPL_Recall(this);
			case PERMANENTGATETRAVEL:
				return new SPL_PermanentGateTravel(this);
			case GATETRAVEL:
				return new SPL_GateTravel(this);
			case FLARE:
				return new SPL_Flare(this);
			case SLOWFIELD:
				return new SPL_Slowfield(this);
			case GRASS:
				return new SPL_Grass(this);
			case ICEBALL:
				return new SPL_Iceball(this);
			case ARROWBLAST:
				return new SPL_ArrowBlast(this);
			case CLANTELEPORT:
				return new SPL_ClanTeleport(this);
			case LIGHT:
				return new SPL_Light(this);
			case HEALSELF:
				return new SPL_HealSelf(this);
			case HEALOTHER:
				return new SPL_HealOther(this);
			case LIGHTNING:
				return new SPL_Lightning(this);
			case FIREFIELD:
				return new SPL_Firefield(this);
			case SUMMONANIMAL:
				return new SPL_SummonAnimal(this);
			case SUMMONMONSTER:
				return new SPL_SummonAnimal(this);
			case BRIDGE:
				return new SPL_Bridge(this);
			case FLOWERS:
				return new SPL_Flowers(this);
			case FIREBALL:
				return new SPL_Fireball(this);
			case FIREWALK:
				return new SPL_FireWalk(this);
			case CREATEFOOD:
				return new SPL_CreateFood(this);
			case DETECTHIDDEN:
				return new SPL_DetectHidden(this);
			case STONESKIN:
				return new SPL_StoneSkin(this);
			case MOONJUMP:
				return new SPL_MoonJump(this);
			case CHRONOPORT:
				return new SPL_Chronoport(this);
			case FORCEPUSH:
				return new SPL_Forcepush(this);
			case WALLOFSTONE:
				return new SPL_WallOfStone(this);
			default:
				return null;
		}
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpellWords() {
		return spellWords;
	}

	public void setSpellWords(String spellWords) {
		this.spellWords = spellWords;
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

	public int getMinMagery() {
		return minMagery;
	}

	public void setMinMagery(int minMagery) {
		this.minMagery = minMagery;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getCastingDelay() {
		return castingDelay;
	}

	public void setCastingDelay(int castingDelay) {
		this.castingDelay = castingDelay;
	}

	public List<ItemStack> getReagentCost() {
		return reagentCost;
	}

	public void setReagentCost(List<ItemStack> reagentCost) {
		this.reagentCost = reagentCost;
	}

	public static Scroll getByString(String string) {
		for(Scroll st : values())
			if(st.name().equalsIgnoreCase(string.trim().replace(" ", "")))
				return st;
		return null;
	}
}