package com.lostshard.RPG.Spells;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Groups.Parties.Party;
import com.lostshard.RPG.Utils.AimBlock;
import com.lostshard.RPG.Utils.Bresenham;
import com.lostshard.RPG.Utils.IntPoint;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Spell {
	public static HashSet<Byte> invisibleBlocks = new HashSet<Byte>();
	private int _id = -1;
	
	public int getId() {
		return _id;
	}
	
	public static HashSet<Byte> getInvisibleBlocks() {
		return invisibleBlocks;
	}
	
	public static void init() {
		invisibleBlocks.add((byte)Material.AIR.getId());
		invisibleBlocks.add((byte)Material.SNOW.getId());
		invisibleBlocks.add((byte)Material.FIRE.getId());
		invisibleBlocks.add((byte)Material.TORCH.getId());
		invisibleBlocks.add((byte)Material.LADDER.getId());
		invisibleBlocks.add((byte)Material.RED_MUSHROOM.getId());
		invisibleBlocks.add((byte)Material.RED_ROSE.getId());
		invisibleBlocks.add((byte)Material.YELLOW_FLOWER.getId());
		invisibleBlocks.add((byte)Material.BROWN_MUSHROOM.getId());
		invisibleBlocks.add((byte)Material.REDSTONE_WIRE.getId());
		invisibleBlocks.add((byte)Material.WOOD_PLATE.getId());
		invisibleBlocks.add((byte)Material.STONE_PLATE.getId());
		invisibleBlocks.add((byte)Material.PORTAL.getId());
		invisibleBlocks.add((byte)Material.LAVA.getId());
		invisibleBlocks.add((byte)Material.WATER.getId());
		invisibleBlocks.add((byte)Material.STONE_BUTTON.getId());
		invisibleBlocks.add((byte)Material.LEVER.getId());
		invisibleBlocks.add((byte)Material.SNOW.getId());
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public String getName() {
		return "GenericName";
	}
	
	public String getReagentString() {
		return "(Dirt)";
	}
	
	public int getPageNumber() {
		return 1;
	}
	
	public int getCastingDelay() {
		return 0;
	}
	
	public int getCooldownTicks() {
		return 0;
	}
	
	public int getManaCost() {
		return 0;
	}
	
	public int[] getReagentCost() {
		int[] f = {};
		return f;
	}
	
	public int getMinMagery() {
		return 0;
	}
	
	public String getSpellWords() {
		return "GenericSpellWords";
	}
	
	public boolean isWandable() {
		return true;
	}
	
	public String getPrompt() {
		return null;
	}
	
	public void setResponse(String response) {
		
	}
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		return true;
	}
	
	public void preAction(Player player) {
		System.out.println("Attempting to preAction Spell");
	}
	
	public void doAction(Player player) {
		System.out.println("Attempting to doAction Spell");
	}
	
	protected static boolean isValidRuneLocation(Player player, Location location) {
		/*Plot plot = PlotHandler.findPlotAt(location);
		if(plot != null) {
			if(!plot.isCity() && !plot.isMember(player.getName())) {
				Output.simpleError(player, "Invalid location, plot protected - not a member.");
				return false;
			}
		}*/
		
		Block blockAt = location.getBlock();
		Block blockAbove = blockAt.getRelative(0,1,0);
		if(!invisibleBlocks.contains((byte)blockAt.getType().getId()) || !invisibleBlocks.contains((byte)blockAbove.getType().getId())) {
			Output.simpleError(player, "Invalid location, blocked.");
			return false;
		}
		
		return true;
	}
	
	protected static Block blockInLOS(Player player, int range) {
		/*ArrayList<Player> playersInRange = new ArrayList<Player>();
		Player[] playerList = Utils.getPlugin().getServer().getOnlinePlayers();
		for(Player p : playerList) {
			if(!p.equals(player)) {
				if(Utils.isWithin(p.getLocation(), player.getLocation(), range))
					playersInRange.add(p);
			}
		}*/
		
		/*AimBlock aimHit = new AimBlock(player, range, .5);
		if(aimHit.getTargetBlock() == null)
			return aimHit.getPreviousBlock();
		return aimHit.getTargetBlock();*/
		Block targetBlock = player.getTargetBlock(invisibleBlocks, range);
		return targetBlock;
		
		/*HitBlox aimHit = new HitBlox(player, range, 1);
		Block b = aimHit.getNextBlock();
		while(b!=null) {
			if(!b.getType().equals(Material.AIR))
				return aimHit.getCurBlock();
			/*for(Player p : playersInRange) {
				if(!p.equals(player)) {
					if(Utils.isWithin(p.getLocation(), player.getLocation(), range))
						return b.getWorld().getBlockAt((int)Math.floor(p.getLocation().getX()), (int)Math.floor(p.getLocation().getY()), (int)Math.floor(p.getLocation().getZ()));
				}
			}*/
			//b=aimHit.getNextBlock();
		//}
	}
	
	protected static List<Block> getLastTwoTargetBlocks(Player player, int range) {
		return player.getLastTwoTargetBlocks(invisibleBlocks, range);
	}
	
	protected static Player playerInLOS(Player player, PseudoPlayer pseudoPlayer, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		
		Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		ArrayList<Player> playersInRange = new ArrayList<Player>();
		for(Player p : onlinePlayers) {
			if(p != player) {
				if(Utils.isWithin(player.getLocation(), p.getLocation(), range)) {
					playersInRange.add(p);
				}
			}
		}
		
		if(playersInRange.size() <= 0)
			return null;
		
		Clan clan = pseudoPlayer.getClan();
		Party party = pseudoPlayer.getParty();
		
		boolean done = false;
		Player _firstNeutralFound = null;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(Player p : playersInRange) {
					if(Utils.isWithin(b.getLocation(), p.getLocation(), 1.5)) {
						if(((clan != null) && clan.isInClan(p.getName())) || ((party != null) && (party.isMember(p.getName())))) {
							return p;
						}
						
						if(_firstNeutralFound == null)
							_firstNeutralFound = p;
					}
				}
			}
			else return _firstNeutralFound;
		}
		
		return _firstNeutralFound;
	}
	
	protected static ArrayList<LivingEntity> enemiesInLOS(Player player, PseudoPlayer pseudoPlayer, int range) {
		AimBlock aimHit = new AimBlock(player, range, .5);
		
		Clan clan = pseudoPlayer.getClan();
		Party party = pseudoPlayer.getParty();
		
		Player[] onlinePlayers = Utils.getPlugin().getServer().getOnlinePlayers();
		List<LivingEntity> livingEntitiesInWorld = player.getWorld().getLivingEntities();
		ArrayList<LivingEntity> livingEntities = new ArrayList<LivingEntity>();
		
		for(Player p : onlinePlayers) {
			livingEntities.add(p);
		}
		
		for(LivingEntity lE : livingEntitiesInWorld) 
		{
			livingEntities.add(lE);
		}
		
		ArrayList<LivingEntity> livingEntitiesInRange = new ArrayList<LivingEntity>();
		for(LivingEntity lE : livingEntities) {
			if(lE instanceof Player) {
				Player p = (Player)lE;
				if(p == player)
					continue;
				if(clan != null) {
					if(clan.isInClan(p.getName())) {
						continue;
					}
				}
				if(party != null) {
					if(party.isMember(p.getName())) {
						continue;
					}
				}
				
				if(Utils.isWithin(player.getLocation(), p.getLocation(), range)) {
						livingEntitiesInRange.add(p);
				}
			}
			else {
				if(Utils.isWithin(player.getLocation(), lE.getLocation(), range)) {
					livingEntitiesInRange.add(lE);
				}
			}
		}
		
		
		ArrayList<LivingEntity> validTargets = new ArrayList<LivingEntity>();
		if(livingEntitiesInRange.size() <= 0)
			return validTargets;
		
		boolean done = false;
		while (!done) {
			Block b = aimHit.getNextBlock();
			if(b != null) {
				for(LivingEntity lE : livingEntitiesInRange) {
					if(Utils.isWithin(b.getLocation(), lE.getLocation(), 3)) {
						validTargets.add(lE);
					}
				}
			}
			else return validTargets;
		}
		
		return validTargets;
	}
	
	protected static Block faceBlockInLOS(Player player, int range) {
		//HitBlox aimHit = new HitBlox(player, range, 1);
		AimBlock aimHit = new AimBlock(player, range, .5);
		return aimHit.getFaceBlock();
		/*Block b = aimHit.getNextBlock();
		ArrayList<Block> blocks = new ArrayList<Block>();
		while(b!=null) {
			//blocks.add(b);
			if(!b.getType().equals(Material.AIR))
				return blocks.get(blocks.size()-2);
			b=aimHit.getNextBlock();
		}*/
	}
	
	public static boolean hasLOSTo(Entity player, Block block) {
		IntPoint playerIntPoint = new IntPoint((int)Math.round(player.getLocation().getX()), (int)Math.round(player.getLocation().getY()+2), (int)Math.round(player.getLocation().getZ()));
		ArrayList<IntPoint> losPoints = Bresenham.bresenham3d(block.getX(), block.getY(), block.getZ(), playerIntPoint.x, playerIntPoint.y, playerIntPoint.z);
		int numLosPoints = losPoints.size();
		for(int f=1; f<numLosPoints; f++) {
			IntPoint intPoint = losPoints.get(f);
			int blockTypeId = block.getWorld().getBlockTypeIdAt(intPoint.x, intPoint.y, intPoint.z);
			if(blockTypeId != Material.AIR.getId()) {
				return false;
			}
		}
		return true;
	}
	
	protected static ArrayList<Block> removeCantSee(Player player, ArrayList<Block> blocks) {
		int numBlocks = blocks.size();
		IntPoint playerIntPoint = new IntPoint((int)Math.round(player.getLocation().getX()), (int)Math.round(player.getLocation().getY())+1, (int)Math.round(player.getLocation().getZ()));
		for(int i=numBlocks-1; i>=0; i--) {
			Block block = blocks.get(i);
			ArrayList<IntPoint> losPoints = Bresenham.bresenham3d(block.getX(), block.getY(), block.getZ(), playerIntPoint.x, playerIntPoint.y, playerIntPoint.z);
			int numLosPoints = losPoints.size();
			for(int f=1; f<numLosPoints; f++) {
				IntPoint intPoint = losPoints.get(f);
				int blockTypeId = block.getWorld().getBlockTypeIdAt(intPoint.x, intPoint.y, intPoint.z);
				if(blockTypeId != Material.AIR.getId()) {
					blocks.remove(i);
					break;
				}
			}
		}
		return blocks;
	}
	
	public static Spell getSpellByName(String spellName) {
		if(spellName.equalsIgnoreCase("Teleport"))
			return new SPL_Teleport();
		else if(spellName.equalsIgnoreCase("Magic Arrow"))
			return new SPL_MagicArrow();
		else if(spellName.equalsIgnoreCase("Fire Field"))
			return new SPL_Firefield();
		else if(spellName.equalsIgnoreCase("Ice Ball"))
			return new SPL_Iceball();
		else if(spellName.equalsIgnoreCase("Grass"))
			return new SPL_Grass();
		else if(spellName.equalsIgnoreCase("Flowers"))
			return new SPL_Flowers();
		else if(spellName.equalsIgnoreCase("Light"))
			return new SPL_Light();
		else if(spellName.equalsIgnoreCase("Create Food"))
			return new SPL_CreateFood();
		else if(spellName.equalsIgnoreCase("Mark"))
			return new SPL_Mark();
		else if(spellName.equalsIgnoreCase("Recall"))
			return new SPL_Recall();
		else if(spellName.equalsIgnoreCase("Gate Travel"))
			return new SPL_GateTravel();
		else if(spellName.equalsIgnoreCase("Bridge"))
			return new SPL_Bridge();
		else if(spellName.equalsIgnoreCase("Heal Other"))
			return new SPL_Heal();
		else if(spellName.equalsIgnoreCase("Heal Self"))
			return new SPL_HealSelf();
		else if(spellName.equalsIgnoreCase("Permanent Gate Travel"))
			return new SPL_PermanentGateTravel();
		else if(spellName.equalsIgnoreCase("Sand Storm"))
			return new SPL_SandStorm();
		else if(spellName.equalsIgnoreCase("Clan Teleport"))
			return new SPL_ClanTeleport();
		else if(spellName.equalsIgnoreCase("Fire Walk"))
			return new SPL_FireWalk();
		else if(spellName.equalsIgnoreCase("Slow Fall"))
			return new SPL_SlowFall();
		/*else if(spellName.equalsIgnoreCase("Fly"))
			return new SPL_Fly();*/
		else if(spellName.equalsIgnoreCase("Lightning"))
			return new SPL_Lightning();
		else if(spellName.equalsIgnoreCase("Fireball"))
			return new SPL_Fireball();
		else if(spellName.equalsIgnoreCase("Day"))
			return new SPL_Day();
		else if(spellName.equalsIgnoreCase("Clear Sky"))
			return new SPL_ClearSky();
		else if(spellName.equalsIgnoreCase("Moon Jump"))
			return new SPL_MoonJump();
		else if(spellName.equalsIgnoreCase("Summon Animal"))
			return new SPL_SummonAnimal();
		else if(spellName.equalsIgnoreCase("Summon Monster"))
			return new SPL_SummonMonster();
		else if(spellName.equalsIgnoreCase("Stone Skin"))
			return new SPL_StoneSkin();
		else if(spellName.equalsIgnoreCase("Meteor Storm"))
			return new SPL_MeteorStorm();
		else if(spellName.equalsIgnoreCase("Slow Field"))
			return new SPL_SlowField();
		else if(spellName.equalsIgnoreCase("Wall of Stone"))
			return new SPL_WallOfStone();
		else if(spellName.equalsIgnoreCase("Blizzard"))
			return new SPL_Blizzard();
		else if(spellName.equalsIgnoreCase("Summon Fire Elemental"))
			return new SPL_SummonFireElemental();
		else if(spellName.equalsIgnoreCase("Force Push"))
			return new SPL_ForcePush();
		else if(spellName.equalsIgnoreCase("Greed"))
			return new SPL_DRG_Greed();
		else if(spellName.equalsIgnoreCase("Gluttony"))
			return new SPL_DRG_Gluttony();
		else if(spellName.equalsIgnoreCase("Sloth"))
			return new SPL_DRG_Sloth();
		else if(spellName.equalsIgnoreCase("Wrath"))
			return new SPL_DRG_Wrath();
		else if(spellName.equalsIgnoreCase("Envy"))
			return new SPL_DRG_Envy();
		else if(spellName.equalsIgnoreCase("Pride"))
			return new SPL_DRG_Pride();
		else if(spellName.equalsIgnoreCase("Lust"))
			return new SPL_DRG_Lust();
		else if(spellName.equalsIgnoreCase("Grief"))
			return new SPL_DRG_Grief();
		else if(spellName.equalsIgnoreCase("Chronoport"))
			return new SPL_Chronoport();
		else if(spellName.equalsIgnoreCase("Detect Hidden"))
			return new SPL_DetectHidden();
		return null;
	}
}
