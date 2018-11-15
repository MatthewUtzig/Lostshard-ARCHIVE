package com.lostshard.RPG.Spells;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.External.Database;
import com.lostshard.RPG.Plots.Plot;
import com.lostshard.RPG.Plots.PlotHandler;
import com.lostshard.RPG.Skills.Rune;
import com.lostshard.RPG.Skills.Runebook;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_DetectHidden extends Spell {
	private static final String 	_name = "Detect Hidden";
	private static final String 	_spellWords = "Sneakthiefius Discoverus";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 50;
	private static final int[]		_reagentCost = {288,331};
	private static final int 		_minMagery = 480;
	private static final float		_range = 10;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 5; }
	//public boolean isWandable()					 	{ return false; }
		
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {	
		
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		
		for(Player p : Bukkit.getOnlinePlayers()) 
		{
			if(p == player)
				continue;
			
			if(Utils.isWithin(player.getLocation(), p.getLocation(), _range)) 
			{
				if(p.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
					p.removePotionEffect(PotionEffectType.INVISIBILITY);
				}
				/*List<PotionEffect> potionEffects = (List<PotionEffect>) p.getActivePotionEffects();
				int numPotionEffects = potionEffects.size();
				for(int i=numPotionEffects-1; i>=0; i--)
				{
					PotionEffect potionEffect = potionEffects.get(i);
					
					if(potionEffect.getType().equals(PotionEffectType.INVISIBILITY)) 
					{
						potionEffects.remove(i);
						foundInvis = true;
					}
				}
				
				if(foundInvis) 
				{
					p.
					p.getActivePotionEffects().clear();
					p.addPotionEffects(potionEffects);
				}*/
			}
		}
	}
}