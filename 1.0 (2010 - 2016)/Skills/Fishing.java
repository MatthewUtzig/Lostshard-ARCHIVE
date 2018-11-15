package com.lostshard.RPG.Skills;

import java.util.HashSet;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.TreasureMap;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Utils.Output;

public class Fishing extends Skill{	
	private static final int BOAT_SUMMON_RANGE = 3;
	private static final int BOAT_SUMMON_STAMINA_COST = 50;
	public static HashSet<Byte> invisibleBlocks = new HashSet<Byte>();
	private static Random _random = new Random();
	
	public static void handleCommand(Player player, PseudoPlayer pseudoPlayer, String[] split) {
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
		invisibleBlocks.add((byte)Material.STONE_BUTTON.getId());
		invisibleBlocks.add((byte)Material.LEVER.getId());
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer) {
		if(pseudoPlayer.isSkillLocked("fishing"))
			return;
		
		skillGain(player, pseudoPlayer, .2, "fishing", "Fishing");
	}

	public static void callBoat(Player player, PseudoPlayer pseudoPlayer) {		
		int curSkill = pseudoPlayer.getSkill("fishing");
		if(curSkill >= 500) {
			int curStam = pseudoPlayer.getStamina();
			if(curStam >= BOAT_SUMMON_STAMINA_COST) {
				pseudoPlayer.setStamina(curStam-BOAT_SUMMON_STAMINA_COST);
			
				if(Math.random()*1000 < curSkill) {
					boolean placedBoat = false;
					for(int x=player.getLocation().getBlockX()-BOAT_SUMMON_RANGE; x<player.getLocation().getBlockX()+BOAT_SUMMON_RANGE; x++) {
						for(int y=player.getLocation().getBlockY()-BOAT_SUMMON_RANGE; y<player.getLocation().getBlockY()+BOAT_SUMMON_RANGE; y++) {
							for(int z=player.getLocation().getBlockZ()-BOAT_SUMMON_RANGE; z<player.getLocation().getBlockZ()+BOAT_SUMMON_RANGE; z++) {
								Block b = player.getWorld().getBlockAt(x, y, z);
								Block blockAbove = player.getWorld().getBlockAt(x,y+1,z);
								if(b != null && b.getType().equals(Material.STATIONARY_WATER) && blockAbove.getType().equals(Material.AIR)) {
									Output.positiveMessage(player, "You summoned a boat.");
									placedBoat = true;
									possibleSkillGain(player, pseudoPlayer);
									player.getWorld().spawn(new Location(b.getWorld(), b.getX()+.5, b.getY()+.5, b.getZ()+.5), Boat.class);
									break;
								}
								if(placedBoat)
									break;
							}
							if(placedBoat)
								break;
						}
						if(placedBoat)
							break;
					}
					
					if(!placedBoat) {
						Output.simpleError(player, "Could not find a valid location to place a boat.");
					}
				}
				else Output.simpleError(player, "You failed to create the boat.");
			}
			else Output.simpleError(player, "You do not have enough stamina to create a boat.");
		}
		else Output.simpleError(player, "You need 50 skill in fishing to create a boat out of thin air.");
	}
	
	public static void caughtFish(PlayerFishEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		pseudoPlayer._awayTicks = 6000;
		int curSkill = pseudoPlayer.getSkill("fishing");
		if((curSkill < 1000) && (pseudoPlayer.getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal())) {
			int totalSkill = curSkill + 5;
			if(totalSkill > 1000)
				totalSkill = 1000;
			pseudoPlayer.setSkill("fishing", totalSkill);
			Output.gainSkill(player, "Fishing", 5, totalSkill);
			Database.updatePlayerByPseudoPlayer(pseudoPlayer);
		}
		
		
		if(Math.random()<.40) {
			int fishingSkill = pseudoPlayer.getSkill("fishing");
			if(fishingSkill < 250) {
				int numItems = TreasureMap._level0LootTable.length;
				int caughtId = TreasureMap._level0LootTable[_random.nextInt(numItems)];
				player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(caughtId, 1));
			}
			else if(fishingSkill < 500) {
				int numItems = TreasureMap._level1LootTable.length;
				int caughtId = TreasureMap._level1LootTable[_random.nextInt(numItems)];
				player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(caughtId, 1));
			}
			else if(fishingSkill < 750) {
				int numItems = TreasureMap._level2LootTable.length;
				int caughtId = TreasureMap._level2LootTable[_random.nextInt(numItems)];
				player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(caughtId, 1));
			}
			else if(fishingSkill < 1000) {
				int numItems = TreasureMap._level3LootTable.length;
				int caughtId = TreasureMap._level3LootTable[_random.nextInt(numItems)];
				player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(caughtId, 1));
			}
			else {
				int numItems = TreasureMap._level4LootTable.length;
				int caughtId = TreasureMap._level4LootTable[_random.nextInt(numItems)];
				player.getWorld().dropItemNaturally(player.getLocation(), new ItemStack(caughtId, 1));
			}
			
			event.setCancelled(true);
		}
	}
}
