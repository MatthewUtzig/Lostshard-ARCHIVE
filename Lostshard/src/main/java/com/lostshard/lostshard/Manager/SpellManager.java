package com.lostshard.lostshard.Manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.ChatHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.SPL_HealOther;
import com.lostshard.lostshard.Spells.SPL_HealSelf;
import com.lostshard.lostshard.Spells.SPL_Lightning;
import com.lostshard.lostshard.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.SPL_PermanentGateTravel;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class SpellManager {

	static SpellManager manager = new SpellManager();
	static PlayerManager pm = PlayerManager.getManager();
	static PlotManager ptm = PlotManager.getManager();
	
	private static double _minChanceToCast = .2;
	private static double _minPercentChanceSkillGain = .2;
//	private static ArrayList<MagicStructure> _magicStructures = new ArrayList<MagicStructure>();
//	public static ArrayList<LightningStrike> _lightning = new ArrayList<LightningStrike>();
	public static int _lastLightningDamageTicks = 0;
	
	public static SpellManager getManager() {
		return manager;
	}

	public Spell getSpellByName(String name) {
		Spell spell = null;
		
		if(name.trim().replace(" ", "").equalsIgnoreCase("healother"))
			spell = new SPL_HealOther();
		else if(name.trim().replace(" ", "").equalsIgnoreCase("healself"))
			spell = new SPL_HealSelf();
		else if(name.trim().replace(" ", "").equalsIgnoreCase("lightning"))
			spell = new SPL_Lightning();
		else if(name.trim().replace(" ", "").equalsIgnoreCase("mark"))
			spell = new SPL_Mark();
		else if(name.trim().replace(" ", "").equalsIgnoreCase("permanentgatetravel"))
			spell = new SPL_PermanentGateTravel();
		return spell;
	}
	
	@SuppressWarnings("deprecation")
	public boolean castSpell(Player player, Spell spell, String message) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
			
		if(pPlayer.getTimer().getCantCastTicks() > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pPlayer.getTimer().getStunTick() > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}
		
		if(pPlayer.getDieLog() > 0) {
			Output.simpleError(player, "You cannot cast a spell right now.");
			return false;
		}
		
		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isAllowMagic()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}
		
		// make sure we even have that spell still
		if(pPlayer.getPromptedSpell() != null)
			pPlayer.setPromptedSpell(null);
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					int blockTypeId = player.getWorld().getBlockTypeIdAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
					if(blockTypeId == Material.LAPIS_BLOCK.getId()) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						pPlayer.getTimer().setCantCastTicks(spell.getCooldownTicks());
						return false;
					}
				}
			}
		}
		
		if(spell.verifyCastable(player, pPlayer)) {
			if(pPlayer.getSpellbook().containSpell(spell.getName())) {
				if(player.isOp() || pPlayer.getMana() >= spell.getManaCost()) {
					if(hasReagents(player, spell.getReagentCost())) {
						int magerySkill = pPlayer.getCurrentBuild().getMagery().getLvl();
						int minMagery = spell.getMinMagery();
						if(magerySkill >= minMagery) {
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
								pPlayer.setMana(pPlayer.getMana()-spell.getManaCost());
//								Database.updatePlayer(player.getName());
								//player.sendMessage(ChatColor.GRAY+"Mana: "+pseudoPlayer.getMana());
								
								// determine if the spell is delayed
								int castingDelay = spell.getCastingDelay();
								if(castingDelay > 0) {
									castDelayed(player, pPlayer, spell);
								}
								else {
									castInstant(player, pPlayer, spell);
								}
								spellGood = true;
							}
							else {
								// spell fizzled, so we want to tell the player if fizzled
								spellFizzled(player, spell);
								// and start the spell cooldown
								pPlayer.getTimer().setCantCastTicks(spell.getCooldownTicks());
							}
							// Whether we succeeded or failed, we have a chance to gain
							possibleSkillGain(player, pPlayer, spell);
							// but you also lose the regs
							if(spell.getReagentCost() == null)
								Output.simpleError(player, "lalala");
							if(((SPL_Lightning) spell).getReagentCost() == null)
								Output.simpleError(player, "blabla");
							takeRegs(player, spell.getReagentCost());
							
//							output the prompt if there is one (name rune, etc)
							if(spell.getPrompt() != null && spellGood) {
								String prompt = spell.getPrompt();
								player.sendMessage(ChatColor.YELLOW+prompt);
							}
						}
						else {
							notSkilledEnough(player, spell); 
							return false;
						}
						pPlayer.getTimer().setCantCastTicks(spell.getCooldownTicks());
					} else {
						notEnoughReagents(player, spell.getReagentCost());
						return false;
					}
				} else {
					notEnoughMana(player, spell);
					return false;
				}
			} else {
				Output.simpleError(player, "Your spellbook does not contain the "+spell.getName()+" spell.");
				return false;
			}
		} else {
			pPlayer.getTimer().setCantCastTicks(spell.getCooldownTicks());
			return false;
		}
		return true;
	}

	private void chant(Player player, String spellWords) {
		player.sendMessage(ChatColor.AQUA+"You chant \""+spellWords+"\".");
		int localRange = ChatHandler.getLocalChatRange();
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(!p.equals(player))
				if(p.getWorld().equals(player.getWorld())) {
					if(Utils.isWithin(player.getLocation(), p.getLocation(), localRange*2))
						p.sendMessage(ChatColor.AQUA+player.getName()+" chants \""+spellWords+"\".");
				}
		}
	}

	private void takeRegs(Player player, ItemStack[] itemCost) {
		if(itemCost == null)
			return;
		for(ItemStack reagent : itemCost) {
			player.getInventory().remove(reagent);
		}
	}

	private void castInstant(Player player, PseudoPlayer pPlayer, Spell spell) {
		if(spell.getPrompt() == null) {
			spell.preAction(player);
			spell.doAction(player);
		}
		else {
			pPlayer.setPromptedSpell(spell);
		}
	}

	private void castDelayed(Player player, PseudoPlayer pPlayer, Spell spell) {
		if(spell.getPrompt() == null) {
			spell.preAction(player);
//			pPlayer.setDelayedSpell(spell);
//			pPlayer.setCastDelayTicks(spell.getCastingDelay());
		}
		else {
			pPlayer.setPromptedSpell(spell);
		}
	}

	private void spellFizzled(Player player, Spell spell) {
		player.sendMessage(ChatColor.DARK_GRAY+"The spell fizzles.");
		player.getWorld().playEffect(player.getLocation(), Effect.EXTINGUISH, 15);
	}

	public static void possibleSkillGain(Player player, PseudoPlayer pseudoPlayer, Spell spell) {
		if(pseudoPlayer.getCurrentBuild().getMagery().isLocked())
			return;
		
		int curSkill = pseudoPlayer.getCurrentBuild().getMagery().getLvl();
		if((curSkill < 1000) && (pseudoPlayer.getCurrentBuild().getTotalSkillVal() < pseudoPlayer.getMaxSkillValTotal())) {
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
				pseudoPlayer.getCurrentBuild().getMagery().setLvl(totalSkill);
				Output.gainSkill(player, "Magery", gainAmount, totalSkill);
//				Database.updatePlayer(player.getName());
			}
		}
	}


	private void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}

	private void notEnoughReagents(Player player, ItemStack[] itemCost) {
		Output.simpleError(player, "You do not have the proper reagents for that spell, they are:");
		for(ItemStack reagent : itemCost) {
			Output.simpleError(player, "-"+reagent.getType().name());
		}}

	private boolean hasReagents(Player player, ItemStack[] itemCost) {
		if(player.isOp())
			return true;
		if(itemCost == null)
			return true;
		for(ItemStack reagent : itemCost) {
			if(!player.getInventory().contains(reagent.getType()))
				return false;
		}
		return true;
	}

	private void notEnoughMana(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough mana to cast "+spell.getName()+".");
	}
}
