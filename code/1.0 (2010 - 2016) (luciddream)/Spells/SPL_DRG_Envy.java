package com.lostshard.RPG.Spells;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_DRG_Envy extends Spell {
	private static final String 	_name = "Envy";
	private static final String 	_spellWords = "Fantasticus Switchius";
	private static final int 		_castingDelay = 0;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 40;
	private static final int[]		_reagentCost = {122, 341};
	private static final int 		_minMagery = 1000;
	private static final int 		_range = 10;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 9; }
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	
	private Player _targetPlayer;
	
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
		Player[] players = Bukkit.getOnlinePlayers();
		ArrayList<Player> inRangePlayers = new ArrayList<Player>();
		ArrayList<PseudoPlayer> inRangePseudoPlayers = new ArrayList<PseudoPlayer>();
		int numPlayers = players.length;
		for(int i=0; i<numPlayers; i++) {
			Player p = players[i];
			if(p == player)
				continue;
			if(Utils.isWithin(p.getLocation(), player.getLocation(), _range)) {
				inRangePlayers.add(p);
				PseudoPlayer pp = PseudoPlayerHandler.getPseudoPlayer(p.getName());
				inRangePseudoPlayers.add(pp);
			}
		}
		
		int numInRangePlayers = inRangePlayers.size();
			if(numInRangePlayers < 1) 
			{
				Output.simpleError(player, "There is nobody near by for you to envy.");
				return false;
			}
		
		int randPlayerIndex = (int)Math.floor(Math.random() * numInRangePlayers);
		if(randPlayerIndex >= numInRangePlayers)
			randPlayerIndex = 0;
		_targetPlayer = inRangePlayers.get(randPlayerIndex);
		
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
		pseudoPlayer.setCantCastTicks(_cooldownTicks);		

		Collection<PotionEffect> potionEffects = player.getActivePotionEffects();
		Collection<PotionEffect> targetPotionEffects = _targetPlayer.getActivePotionEffects();
		
		player.removePotionEffect(PotionEffectType.BLINDNESS);
		player.removePotionEffect(PotionEffectType.CONFUSION);
		player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		player.removePotionEffect(PotionEffectType.HARM);
		player.removePotionEffect(PotionEffectType.HEAL);
		player.removePotionEffect(PotionEffectType.HUNGER);
		player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		player.removePotionEffect(PotionEffectType.JUMP);
		player.removePotionEffect(PotionEffectType.POISON);
		player.removePotionEffect(PotionEffectType.REGENERATION);
		player.removePotionEffect(PotionEffectType.SLOW);
		player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		player.removePotionEffect(PotionEffectType.SPEED);
		player.removePotionEffect(PotionEffectType.WATER_BREATHING);
		player.removePotionEffect(PotionEffectType.WEAKNESS);
		
		_targetPlayer.removePotionEffect(PotionEffectType.BLINDNESS);
		_targetPlayer.removePotionEffect(PotionEffectType.CONFUSION);
		_targetPlayer.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
		_targetPlayer.removePotionEffect(PotionEffectType.FAST_DIGGING);
		_targetPlayer.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		_targetPlayer.removePotionEffect(PotionEffectType.HARM);
		_targetPlayer.removePotionEffect(PotionEffectType.HEAL);
		_targetPlayer.removePotionEffect(PotionEffectType.HUNGER);
		_targetPlayer.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		_targetPlayer.removePotionEffect(PotionEffectType.JUMP);
		_targetPlayer.removePotionEffect(PotionEffectType.POISON);
		_targetPlayer.removePotionEffect(PotionEffectType.REGENERATION);
		_targetPlayer.removePotionEffect(PotionEffectType.SLOW);
		_targetPlayer.removePotionEffect(PotionEffectType.SLOW_DIGGING);
		_targetPlayer.removePotionEffect(PotionEffectType.SPEED);
		_targetPlayer.removePotionEffect(PotionEffectType.WATER_BREATHING);
		_targetPlayer.removePotionEffect(PotionEffectType.WEAKNESS);
		
		player.addPotionEffects(targetPotionEffects);
		_targetPlayer.addPotionEffects(potionEffects);
		
		Damageable damag = player;
		Damageable tDamag = _targetPlayer;
		double curHealth = damag.getHealth();
		double targetHealth = tDamag.getHealth();
		
		player.setHealth(targetHealth);
		_targetPlayer.setHealth(curHealth);
	}
}
