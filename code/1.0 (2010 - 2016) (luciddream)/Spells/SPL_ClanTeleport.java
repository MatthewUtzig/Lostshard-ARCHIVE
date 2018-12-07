package com.lostshard.RPG.Spells;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

import com.lostshard.RPG.PseudoPlayer;
import com.lostshard.RPG.PseudoPlayerHandler;
import com.lostshard.RPG.RPG;
import com.lostshard.RPG.Groups.Clans.Clan;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class SPL_ClanTeleport extends Spell {
	private static final String 	_name = "Clan Teleport";
	private static final String 	_spellWords = "Arg Matius Teleportus";
	private static final int 		_castingDelay = 20;
	private static final int 		_cooldownTicks = 20;
	private static final int		_manaCost = 50;
	private static final int[]		_reagentCost = {288,331};
	private static final int 		_minMagery = 720;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 7; }
	//public boolean isWandable()					 	{ return false; }
	
	String _response;
	public String getPrompt() 						{ return "Who would you like to teleport to?"; }
	public void setResponse(String response)		{ _response = response; }
	
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
		Output.positiveMessage(player, "You begin casting Clan Teleport");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(player.getName());
		Player targetPlayer = Utils.getPlugin().getServer().getPlayer(_response);
		
		if(targetPlayer == null) {
			Output.simpleError(player, "Player not found.");
			pseudoPlayer.setCantCastTicks(_cooldownTicks);
			return;
		}
		else {			
			PseudoPlayer targetPseudoPlayer = PseudoPlayerHandler.getPseudoPlayer(targetPlayer.getName());
			Clan playerClan = pseudoPlayer.getClan();
			Clan targetPlayerClan = targetPseudoPlayer.getClan();
			if((playerClan != null) && (targetPlayerClan != null) && playerClan.getName().endsWith(targetPlayerClan.getName())) {
				if(targetPseudoPlayer.isPrivate()) {
					Output.positiveMessage(player, "Can't teleport to that player, they are set to private.");
					return;
				}
				
				Damageable damag = targetPlayer;
				if(damag.getHealth() <= 0) {
					Output.positiveMessage(player, "You cannot teleport to that player, as they are dead.");
					return;
				}
				
				Utils.loadChunkAtLocation(targetPlayer.getLocation().getBlock().getLocation());
				//check for lapis below you
				if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
				   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot teleport from a Lapis Lazuli block.");
					return;
				}
				
				//check for lapis below your target location
				if(targetPlayer.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
						targetPlayer.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot teleport to a Lapis Lazuli block.");
					return;
				}
				
				if(targetPlayer.getWorld() == RPG._hungryWorld || player.getWorld() == RPG._hungryWorld) {
					Output.simpleError(player, "You can't do that here!");
					return;
				}
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				
				player.teleport(targetPlayer.getLocation());
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				
				pseudoPlayer.setMana(0);
				pseudoPlayer.setStamina(0);
				Output.sendEffectTextNearby(player, "You hear a loud crack and the fizzle of electricity.");
				player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
			}
			else {
				Output.simpleError(player, "That player is not in your clan.");
				pseudoPlayer.setCantCastTicks(_cooldownTicks);
				return;
			}
		}
	}
}
