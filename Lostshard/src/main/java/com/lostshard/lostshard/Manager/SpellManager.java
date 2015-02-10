package com.lostshard.lostshard.Manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.SPL_Heal;
import com.lostshard.lostshard.Spells.SPL_HealSelf;
import com.lostshard.lostshard.Spells.SPL_Lightning;
import com.lostshard.lostshard.Spells.SPL_Mark;
import com.lostshard.lostshard.Spells.SPL_PermanentGateTravel;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Utils.Output;

public class SpellManager {

	static SpellManager manager = new SpellManager();
	
	static PlayerManager pm = PlayerManager.getManager();

	public static SpellManager getManager() {
		return manager;
	}

	public Spell getSpellByName(String name) {
		Spell spell = null;
		
		if(name.equalsIgnoreCase("heal"))
			spell = new SPL_Heal();
		else if(name.equalsIgnoreCase("healself"))
			spell = new SPL_HealSelf();
		else if(name.equalsIgnoreCase("lightning"))
			spell = new SPL_Lightning();
		else if(name.equalsIgnoreCase("mark"))
			spell = new SPL_Mark();
		else if(name.equalsIgnoreCase("permanentgatetravel"))
			spell = new SPL_PermanentGateTravel();
		return spell;
	}
	
	@SuppressWarnings("deprecation")
	public boolean castSpell(Player player, Spell spell, String message) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
			
		if(pPlayer.getCantCastTicks() > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pPlayer.getStunTick() > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}
		
//		if(pseudoPlayer._dieLog > 0) {
//			Output.simpleError(player, "You cannot cast a spell right now.");
//			return false;
//		}
		
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if(plot != null) {
			if(plot.isAllowMagic()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}
		
		// make sure we even have that spell still
//		if(pseudoPlayer.getPromptedSpell() != null)
//			pseudoPlayer.setPromptedSpell(null);
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					int blockTypeId = player.getWorld().getBlockTypeIdAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z);
					if(blockTypeId == Material.LAPIS_BLOCK.getId()) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						pPlayer.setCantCastTicks(spell.getCooldownTicks());
						return false;
					}
				}
			}
		}
		
//		if(spell.verifyCastable(player, pseudoPlayer)) {
//			if(pseudoPlayer.getSpellbook().hasSpellByName(spell.getName())) {
//				if(player.isOp() || pseudoPlayer.getMana() >= spell.getManaCost()) {
//					if(hasReagents(player, spell.getReagentCost())) {
//						int magerySkill = pseudoPlayer.getSkill("magery");
//						int minMagery = spell.getMinMagery();
//						if(magerySkill >= spell.getMinMagery()) {
//							int skillDif = magerySkill - minMagery;
//							double chanceToCast = (double)skillDif / 200;
//							if(chanceToCast < _minChanceToCast)
//								chanceToCast = _minChanceToCast;
//							double rand = Math.random();
//							boolean spellGood = false;
//							if(magerySkill >= 1000 || rand < chanceToCast) {
//								// chant the spell words so people know what you are doing
//								chant(player, spell.getSpellWords());
//								// take mana, we succeeded
//								pseudoPlayer.setMana(pseudoPlayer.getMana()-spell.getManaCost());
//								Database.updatePlayer(player.getName());
//								//player.sendMessage(ChatColor.GRAY+"Mana: "+pseudoPlayer.getMana());
//								
//								// determine if the spell is delayed
//								int castingDelay = spell.getCastingDelay();
//								if(castingDelay > 0) {
//									castDelayed(player, pseudoPlayer, spell);
//								}
//								else {
//									castInstant(player, pseudoPlayer, spell);
//								}
//								spellGood = true;
//							}
//							else {
//								// spell fizzled, so we want to tell the player if fizzled
//								spellFizzled(player, spell);
//								// and start the spell cooldown
//								pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
//							}
//							// Whether we succeeded or failed, we have a chance to gain
//							possibleSkillGain(player, pseudoPlayer, spell);
//							// but you also lose the regs
//							takeRegs(player, spell.getReagentCost());
//							
//							// output the prompt if there is one (name rune, etc)
//							if(spell.getPrompt() != null && spellGood) {
//								String prompt = spell.getPrompt();
//								player.sendMessage(ChatColor.YELLOW+prompt);
//							}
//						}
//						else {
//							notSkilledEnough(player, spell); 
//							return false;
//						}
//						//pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
//					}
//					else {
//						notEnoughReagents(player, spell.getReagentCost());
//						return false;
//					}
//				}
//				else {
//					notEnoughMana(player, spell);
//					return false;
//				}
//			}
//			else {
//				Output.simpleError(player, "Your spellbook does not contain the "+spell.getName()+" spell.");
//				return false;
//			}
//		}
//		else {
//			pseudoPlayer.setCantCastTicks(spell.getCooldownTicks());
//			return false;
//		}
		return true;
	}

	
}
