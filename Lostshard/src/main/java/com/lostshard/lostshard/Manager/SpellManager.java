package com.lostshard.lostshard.Manager;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Handlers.ChatHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Spells.Spell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.ItemUtils;
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
		Scroll st = Scroll.getByString(name);
		if(st != null)
			return st.getSpell();
		return null;
	}
	
	public boolean useScroll(Player player, Spell spell) {
		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot != null) {
			if(!plot.isAllowMagic()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}
		
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		
		if(pseudoPlayer.getTimer().cantCastTicks > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pseudoPlayer.getTimer().stunTick > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					Material blockMat = player.getWorld().getBlockAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z).getType();
					if(blockMat == Material.LAPIS_BLOCK) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						pseudoPlayer.getTimer().cantCastTicks = spell.getCooldown();
						return false;
					}
				}
			}
		}
		
		// make sure we even have that spell still
		if(spell.verifyCastable(player)) {
			if(player.isOp() || pseudoPlayer.getMana() >= spell.getManaCost()) {
				// chant the spell words so people know what you are doing
				chant(player, spell.getSpellWords());
				pseudoPlayer.setMana(pseudoPlayer.getMana()-spell.getManaCost());
				pseudoPlayer.update();
				
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
			pseudoPlayer.getTimer().cantCastTicks = spell.getCooldown();
			return false;
		}
		return true;
	}
	
	public boolean castSpell(Player player, Spell spell) {
		PseudoPlayer pPlayer = pm.getPlayer(player);
		
		if(pPlayer.getTimer().cantCastTicks > 0) {
			Output.simpleError(player, "You cannot cast another spell again so soon.");
			return false;
		}
		
		if(pPlayer.getTimer().stunTick > 0) {
			Output.simpleError(player, "You cannot use magic while stunned.");
			return false;
		}

		if(pPlayer.getDieLog() > 0) {
			Output.simpleError(player, "You cannot cast a spell right now.");
			return false;
		}

		Plot plot = ptm.findPlotAt(player.getLocation());
		if(plot != null) {
			if(!plot.isAllowMagic()) {
				Output.simpleError(player, "You cannot use magic in "+plot.getName()+".");
				return false;
			}
		}

		if(pPlayer.getPromptedSpell() != null) {
			Output.simpleError(player, "You are already casting a spell.");
			return false;
		}

		if(!pPlayer.getSpellbook().containSpell(spell.getType())) {
			Output.simpleError(player, "Your spellbook does not contain the "+spell.getName()+" spell.");
			return false;
		}

		pPlayer.getTimer().cantCastTicks = spell.getCooldown();
		
		Location loc = player.getLocation();
		
		for(int x=-2; x<= 2; x++) {
			for(int y=-2; y<= 2; y++) {
				for(int z=-2; z<= 2; z++) {
					Material type = player.getWorld().getBlockAt(loc.getBlockX()+x,loc.getBlockY()+y,loc.getBlockZ()+z).getType();
					if(type == Material.LAPIS_BLOCK) {
						Output.simpleError(player, "You cannot seem to cast a spell here...");
						return false;
					}
				}
			}
		}
		
		if(!spell.verifyCastable(player)) {
			return false;
		}
		
		if(!(player.isOp() || pPlayer.getMana() >= spell.getManaCost())) {
			notEnoughMana(player, spell);
			return false;
		}
		
		if(!hasReagents(player, spell.getReagentCost())) {
			notEnoughReagents(player, spell.getReagentCost());
			return false;
		}
		
		int magerySkill = pPlayer.getCurrentBuild().getMagery().getLvl();
		int minMagery = spell.getMinMagery();
		
		if(!(magerySkill >= minMagery)) {
			notSkilledEnough(player, spell); 
			return false;
		}
			
		int skillDif = magerySkill - minMagery;
		double chanceToCast = (double)skillDif / 200;
		if(chanceToCast < _minChanceToCast)
			chanceToCast = _minChanceToCast;
		double rand = Math.random();
		
		if(!(magerySkill >= 1000 || rand < chanceToCast)) {
			// spell fizzled, so we want to tell the player if fizzled
			spellFizzled(player, spell);
			// and start the spell cooldown
			pPlayer.getTimer().cantCastTicks = spell.getCooldown();
			return false;
		}
			// chant the spell words so people know what you are doing
			chant(player, spell.getSpellWords());
			// take mana, we succeeded
			pPlayer.setMana(pPlayer.getMana()-spell.getManaCost());
			
			// determine if the spell is delayed
			int castingDelay = spell.getCastingDelay();
			if(castingDelay > 0) {
				castDelayed(player, pPlayer, spell);
			}
			else {
				castInstant(player, pPlayer, spell);
			}
		// Whether we succeeded or failed, we have a chance to gain
		possibleSkillGain(player, pPlayer, spell);
		// but you also lose the regs
		takeRegs(player, spell.getReagentCost());
						
//		output the prompt if there is one (name rune, etc)
		if(spell.getPrompt() != null) {
			String prompt = spell.getPrompt();
			player.sendMessage(ChatColor.YELLOW+prompt);
		}
		return true;
	}

	public void chant(Player player, String spellWords) {
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

	public void takeRegs(Player player, List<ItemStack> list) {
		if(list == null)
			return;
		for(ItemStack reagent : list)
			ItemUtils.removeItem(player.getInventory(), reagent.getType(), reagent.getAmount());
	}

	public void castInstant(Player player, PseudoPlayer pPlayer, Spell spell) {
		if(spell.getPrompt() == null) {
			spell.preAction(player);
			pPlayer.getTimer().cantCastTicks = spell.getCooldown();
			spell.doAction(player);
		}
		else {
			pPlayer.setPromptedSpell(spell);
		}
	}

	public void castDelayed(Player player, PseudoPlayer pPlayer, Spell spell) {
		if(spell.getPrompt() == null) {
			spell.preAction(player);
			pPlayer.getTimer().delayedSpell = spell;
		}
		else {
			pPlayer.setPromptedSpell(spell);
		}
	}

	public void spellFizzled(Player player, Spell spell) {
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


	public void notSkilledEnough(Player player, Spell spell) {
		Output.simpleError(player, "You are not skilled enough at magery to cast "+spell.getName()+".");
	}

	public void notEnoughReagents(Player player, List<ItemStack> list) {
		Output.simpleError(player, "You do not have the proper reagents for that spell, they are:");
		for(ItemStack reagent : list) {
			Output.simpleError(player, "-"+reagent.getType().name());
		}
	}

	public boolean hasReagents(Player player, List<ItemStack> list) {
		if(player.isOp())
			return true;
		if(list == null)
			return true;
		for(ItemStack reagent : list) {
			if(!player.getInventory().contains(reagent.getType(), reagent.getAmount()))
				return false;
		}
		return true;
	}

	public void notEnoughMana(Player player, Spell spell) {
		Output.simpleError(player, "You do not have enough mana to cast "+spell.getName()+".");
	}

	public static void onPlayerPromt(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		String message = event.getMessage();
		Spell spell = pPlayer.getPromptedSpell();
		spell.setResponse(message);
		
		int castingDelay = spell.getCastingDelay();
			if(castingDelay > 0) {
				spell.preAction(player);
				pPlayer.getTimer().delayedSpell = spell;
			}
			else {
				spell.preAction(player);
				pPlayer.getTimer().cantCastTicks = spell.getCooldown();
				spell.doAction(player);
			}
		pPlayer.setPromptedSpell(null);
		event.setCancelled(true);
	}
	
	public static void move(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(event.getTo() != null && event.getFrom() != null && event.getTo().getBlock() != event.getFrom().getBlock()) {
			if(pPlayer.getPromptedSpell() != null || pPlayer.getTimer().delayedSpell != null) {
				pPlayer.setPromptedSpell(null);
				pPlayer.getTimer().delayedSpell = null;
				Output.simpleError(player, "Moved while casting a spell, it was disrupted.");
			}else if(pPlayer.getTimer().goToSpawnTicks > 0) {
				pPlayer.getTimer().goToSpawnTicks = 0;
				Output.simpleError(player, "Moved while casting, /spawn was disrupted.");
			}
		}
	}
}
