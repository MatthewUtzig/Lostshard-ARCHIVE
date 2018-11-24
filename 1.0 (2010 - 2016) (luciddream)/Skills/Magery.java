package com.lostshard.RPG.Skills;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Spells.*;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Magery extends Skill{
	private static double _minChanceToCast = .2;
	private static double _minPercentChanceSkillGain = .2;
	private static ArrayList<MagicStructure> _magicStructures = new ArrayList<MagicStructure>();
	public static ArrayList<LightningStrike> _lightning = new ArrayList<LightningStrike>();
	public static int _lastLightningDamageTicks = 0;
	
	public static void handleCommand(Player player, String[] split) {
		//if(split[0].equals("/magery")) {
			if(split.length == 1) {
				player.sendMessage(ChatColor.GOLD+"-Magery Commands-");
				player.sendMessage(ChatColor.YELLOW+"bind (spell name)"+ChatColor.WHITE+"Binds a spell to the current slot.");
			}
			else if(split.length > 1) {
				String secondaryCommand = split[1];
				if(secondaryCommand.equalsIgnoreCase("test")) {
					System.out.println("magicstructures: " + _magicStructures.size());
				}
				if(secondaryCommand.equals("bind")) {
					if(split.length > 2) {
						// attempting to bind a spell
						int activeSlot = player.getInventory().getHeldItemSlot();
						int numWords = split.length;
						String spellName = "";
						for(int i=2; i<numWords; i++) {
							spellName += split[i] + " ";
						}
						spellName = spellName.trim();
						
						PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
						Spell spell = pseudoPlayer.getSpellbook().getSpellByName(spellName);
						if(spell != null) {
							if(spell.isWandable()) {
								pseudoPlayer.setSpellInSlot(activeSlot, spell);
								Database.updateSpellBind(pseudoPlayer, activeSlot, spell);
								Output.positiveMessage(player, "You have bound "+spell.getName()+" to slot "+(activeSlot+1)+".");
							}
							else Output.simpleError(player, "Cannot bind "+spell.getName()+" to a wand.");
						}
						else Output.simpleError(player, "Your spellbook does not contain the "+spellName+" spell.");
					}
					else {
						// just typing /magery bind
						Output.simpleError(player, "Use \"/magery bind (spell name)\" to bind a spell to a slot");
					}
				}
				else if(secondaryCommand.equals("cast") || secondaryCommand.equalsIgnoreCase("use")) {
					if(split.length > 2) {
						// attempting to cast a spell
						
						int numWords = split.length;
						
						PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
						
						String spellName = "";
						for(int i=2; i<numWords; i++) {
							spellName += split[i] + " ";
						}
						spellName = spellName.trim();
						
						Spell spell = pseudoPlayer.getSpellbook().getSpellByName(spellName);
						if(spell != null) {
							castSpell(player, spell, "");
						}
						else Output.simpleError(player, "Your spellbook does not contain the "+spellName+" spell.");
					}
					else {
						// just typing /magery bind
						Output.simpleError(player, "Refer to the guide on www.lostshard.com for casting spells.");
					}
				}
			}
		//}
	}
	
	//private static int debugTickCount = 0;
	private static int _count = 0;
	public static void tick() {
		_count++;
		// sort through all the magic structures and remove them if they are dead
		int numMagicStructures = _magicStructures.size();
		for(int i=numMagicStructures-1; i>=0; i--) {
			MagicStructure ms = _magicStructures.get(i);
			ms.tick();
			if(ms.isDead()) {
				_magicStructures.remove(i);
			}
			else  {
				if(_count % 200 == 0) {
					if(ms instanceof PermanentGate) {
						ArrayList<Block> blocks = ms.getBlocks();
						for(Block b : blocks) {
							//Utils.loadChunkAtLocation(b.getLocation());
							if(b.getWorld().isChunkLoaded(b.getChunk())) {
								b.setType(Material.PORTAL);
							}
						}
					}
				}
			}
		}
	}
	
	// The purpose of this function is to determine if any of the magic structures currently
	// in existance have any blocks located at a specific position. The goal of this is to 
	// figure out whether a player is doing damage with a magic structure
	public static String findCreatorNameOfBlockAt(int x, int y, int z) {
		// loop through all the magic structures
		for(MagicStructure magicStructure : _magicStructures) {
			// get the block at the location (if there is one)
			Block b = magicStructure.findBlockAt(x,y,z);
			// if there is one, return the magic structure's owner
			if(b != null)
				return magicStructure.getCreatorName();
		}
		return null;
	}
	
	public static boolean castSpell(Player player, Spell spell, String message) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		
		if(pseudoPlayer.getCantCastTicks() > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pseudoPlayer._stunTicks > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}
		
		if(pseudoPlayer._dieLog > 0) {
			Output.simpleError(player, "You cannot cast a spell right now.");
			return false;
		}
		
		if(pseudoPlayer._stunTicks > 0) {
			Output.simpleError(player, "Cannot use magic, you are stunned!");
			return false;
		}
		
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isNoMagicPlot()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}
		
		// make sure we even have that spell still
		if(pseudoPlayer.getPromptedSpell() != null)
			pseudoPlayer.setPromptedSpell(null);
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					int blockTypeId = player.getWorld().getBlockTypeIdAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
					if(blockTypeId == Material.LAPIS_BLOCK.getId()) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
						return false;
					}
				}
			}
		}
		
		if(spell.verifyCastable(player, pseudoPlayer)) {
			if(pseudoPlayer.getSpellbook().hasSpellByName(spell.getName())) {
				if(player.isOp() || pseudoPlayer.getMana() >= spell.getManaCost()) {
					if(hasReagents(player, spell.getReagentCost())) {
						int magerySkill = pseudoPlayer.getSkill("magery");
						int minMagery = spell.getMinMagery();
						if(magerySkill >= spell.getMinMagery()) {
							int skillDif = magerySkill - minMagery;
							double chanceToCast = (double)skillDif / 200;
							if(chanceToCast < _minChanceToCast)
								chanceToCast = _minChanceToCast;
							double rand = Math.random();
							boolean spellGood = false;
							if(magerySkill >= 1000 || rand < chanceToCast) {
								// chant the spell words so people know what you are doing
								chant(player, spell.getSpellWords());
								// take mana, we succeeded
								pseudoPlayer.setMana(pseudoPlayer.getMana()-spell.getManaCost());
								Database.updatePlayer(player.getName());
								//player.sendMessage(ChatColor.GRAY+"Mana: "+pseudoPlayer.getMana());
								
								// determine if the spell is delayed
								int castingDelay = spell.getCastingDelay();
								if(castingDelay > 0) {
									castDelayed(player, pseudoPlayer, spell);
								}
								else {
									castInstant(player, pseudoPlayer, spell);
								}
								spellGood = true;
							}
							else {
								// spell fizzled, so we want to tell the player if fizzled
								spellFizzled(player, spell);
								// and start the spell cooldown
								pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
							}
							// Whether we succeeded or failed, we have a chance to gain
							possibleSkillGain(player, pseudoPlayer, spell);
							// but you also lose the regs
							takeRegs(player, spell.getReagentCost());
							
							// output the prompt if there is one (name rune, etc)
							if(spell.getPrompt() != null && spellGood) {
								String prompt = spell.getPrompt();
								player.sendMessage(ChatColor.YELLOW+prompt);
							}
						}
						else {
							notSkilledEnough(player, spell); 
							return false;
						}
						//pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
					}
					else {
						notEnoughReagents(player, spell.getReagentCost());
						return false;
					}
				}
				else {
					notEnoughMana(player, spell);
					return false;
				}
			}
			else {
				Output.simpleError(player, "Your spellbook does not contain the "+spell.getName()+" spell.");
				return false;
			}
		}
		else {
			pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
			return false;
		}
		return true;
	}
	
	public static boolean useScroll(Player player, Spell spell) {
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isNoMagicPlot()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}
		
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		
		if(pseudoPlayer.getCantCastTicks() > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pseudoPlayer._stunTicks > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					int blockTypeId = player.getWorld().getBlockTypeIdAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
					if(blockTypeId == Material.LAPIS_BLOCK.getId()) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
						return false;
					}
				}
			}
		}
		
		// make sure we even have that spell still
		if(spell.verifyCastable(player, pseudoPlayer)) {
			if(player.isOp() || pseudoPlayer.getMana() >= spell.getManaCost()) {
				// chant the spell words so people know what you are doing
				chant(player, spell.getSpellWords());
				pseudoPlayer.setMana(pseudoPlayer.getMana()-spell.getManaCost());
				Database.updatePlayer(player.getName());
				
				// determine if the spell is delayed
				int castingDelay = spell.getCastingDelay();
				boolean spellGood = false;
				if(castingDelay > 0) {
					castDelayed(player, pseudoPlayer, spell);
					spellGood = true;
				}
				else {
					castInstant(player, pseudoPlayer, spell);
					spellGood = true;
				}
				
				if(spell.getPrompt() != null && spellGood) {
					String prompt = spell.getPrompt();
					player.sendMessage(ChatColor.YELLOW+prompt);
				}
			}
			else {
				notEnoughMana(player, spell);
				return false;
			}
		}
		else {
			pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
			return false;
		}
		return true;
	}
	
	public static void addMagicStructure(MagicStructure ms) {
		_magicStructures.add(ms);
	}
	
	public static ArrayList<MagicStructure> getMagicStructures() {
		return _magicStructures;
	}
	
	private static void castDelayed(Player player, PseudoPlayer pseudoPlayer, Spell spell) {
		//player.sendMessage(ChatColor.GRAY+"You begin casting a spell...");
		if(spell.getPrompt() == null) {
			spell.preAction(player);
			pseudoPlayer.setDelayedSpell(spell);
			pseudoPlayer.setCastDelayTicks(spell.getCastingDelay());
		}
		else {
			pseudoPlayer.setPromptedSpell(spell);
		}
	}
	
	private static void castInstant(Player player, PseudoPlayer pseudoPlayer, Spell spell) {
		if(spell.getPrompt() == null) {
			spell.preAction(player);
			spell.doAction(player);
		}
		else {
			pseudoPlayer.setPromptedSpell(spell);
		}
	}
	
	public static boolean hasReagents(Player player, int[] reagents) {
		for(int reagent : reagents) {
			if(!player.getInventory().contains(reagent))
				return false;
		}
		return true;
	}
	
	public static void notEnoughReagents(Player player, int[] reagents) {
		Output.simpleError(player, "You do not have the proper reagents for that spell, they are:");
		for(int reagent : reagents) {
			Output.simpleError(player, "-"+Material.getMaterial(reagent).name());
		}
	}
	
	public static void takeRegs(Player player, int[] reagents) {
		for(int reagent: reagents) {
			player.getInventory().removeItem(new ItemStack(reagent, 1));
		}
	}
	
	public static void notEnoughMana(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough mana to cast "+spell.getName()+".");
	}
	
	public static void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}
	
	public static void spellFizzled(Player player, Spell spell) {
		player.sendMessage(ChatColor.DARK_GRAY+"The spell fizzles.");
		player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 15);
		
		//player.playNote(player.getLocation(), (byte)3, (byte)5);
	}
	
	public static void chant(Player player, String spellWords) {
		player.sendMessage(ChatColor.AQUA+"You chant \""+spellWords+"\".");
		Player[] onlinePlayers = RPG.getPlugin().getOnlinePlayers();
		int localRange = Output.getLocalRange();
		for(Player p : onlinePlayers) {
			if(!p.equals(player))
				if(p.getWorld().equals(player.getWorld())) {
					if(Utils.isWithin(player.getLocation(), p.getLocation(), localRange*2))
						p.sendMessage(ChatColor.AQUA+player.getName()+" chants \""+spellWords+"\".");
				}
		}
		//Output.messageLocalBarSelf(player, ChatColor.AQUA+player.getName()+" chants \""+spellWords+"\".");
	}
	
	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Spell spell) {
		if(pseudoPlayer.isSkillLocked("magery"))
			return;
		
		int curSkill = pseudoPlayer.getSkill("magery");
		if((curSkill < 1000) && (pseudoPlayer.getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal())) {
			int minMagery = spell.getMinMagery();
			double percentChance;
			if(curSkill < minMagery + 200) {
				// possible to gain (within 20 skill points)
				percentChance = ((double)(1000-curSkill))/1000;
				// we don't want it to be TOOOO hard to gain skill
				if(percentChance < _minPercentChanceSkillGain)
					percentChance = _minPercentChanceSkillGain;
			}
			else {
				// not normally possible to gain but we will let people gain on anything with a small chance
				percentChance = .05; // 5 percent chance to gain from any spell
			}
			double rand = Math.random();
			if(rand < percentChance) {
				int gainAmount;
				// variable gain depending on skill level
				double rand2 = Math.random();
				if(rand2 < .2)
					gainAmount = 1;
				else if(rand2 < .4)
					gainAmount = 2;
				else if(rand2 < .6)
					gainAmount = 3;
				else if(rand2 <= .8)
					gainAmount = 4;
				else
					gainAmount = 5;
				
				// Actually set the gain
				int totalSkill = curSkill + gainAmount;
				if(totalSkill > 1000)
					totalSkill = 1000;
				pseudoPlayer.setSkill("magery", totalSkill);
				Output.gainSkill(player, "Magery", gainAmount, totalSkill);
				Database.updatePlayer(player.getName());
			}
		}
	}
}
