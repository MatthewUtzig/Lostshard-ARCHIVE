package com.lostshard.lostshard.Spells;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public class SPL_Recall extends Spell {

	public SPL_Recall() {
		super();
		setName("Recall");
		setSpellWords("Runus Teleporticus");
		setPrompt("What rune would you like to recall from?");
		setCastingDelay(20);
		setCooldown(20);
		setManaCost(30);
		setMinMagery(360);
		setPage(4);
		addReagentCost(new ItemStack(Material.FEATHER));
	}
	
	public boolean verifyCastable(Player player) {
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Recall...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {		
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = pm.getPlayer(player);
		
		Runebook runebook = pseudoPlayer.getRunebook();
		ArrayList<Rune> runes = runebook.getRunes();
		Rune runeFound = null;
		int count = 0;
		for(Rune rune : runes) {
			if(!player.isOp() && !pseudoPlayer.wasSubscribed() && (count >= 8))
				break;
			if(rune.getLabel().equalsIgnoreCase(getResponse())) {
				runeFound = rune;
				break;
			}
			count++;
		}
		
		boolean usingSpawn = false;
//		if(runeFound == null) {
//				if(getResponse().equalsIgnoreCase("spawn")) {
//					usingSpawn = true;
//					if(pseudoPlayer.isCriminal())
//						runeFound = new Rune("", RPG._murdererSpawn, 0);
//					else 
//						runeFound = new Rune("", RPG._blueSpawn, 0);
//				}
//				else if(getResponse().equalsIgnoreCase("random")) {
//					if(player.getWorld().equals(RPG._normalWorld)) {
//						int randIntX = (int)Math.floor(Math.random()*18000-9000);
//						if(Math.random()<.5)
//							randIntX = -randIntX;
//						int randIntZ = (int)Math.floor(Math.random()*18000-9000);
//						if(Math.random()<.5)
//							randIntZ = -randIntZ;
//						Chunk chunk = player.getWorld().getChunkAt(randIntX, randIntZ);
//						player.getWorld().loadChunk(chunk);
//						int i;
//						for(i=127; i>= 2; i--) {
//							if(player.getWorld().getBlockAt(randIntX, i, randIntZ).getTypeId() == 0)
//								continue;
//							else
//								break;
//						}
//						int highestY = i;
//						Location randLoc = new Location(player.getWorld(),randIntX, highestY+2, randIntZ);
//						System.out.println(randLoc.toString());
//						runeFound = new Rune("", randLoc, 0);
//					}
//					else Output.simpleError(player, "You can only recall to a random location in the normal world.");
//				}
//		}
		
		if(runeFound != null) {
			Location runeLoc = runeFound.getLocation();
			if(!SpellUtils.isValidRuneLocation(player, runeLoc)) {
				return;
			}
			
			Plot plot = ptm.findPlotAt(runeLoc);
			if((plot == null) || !plot.isPrivatePlot() || plot.isFriendOrAbove(player)) {				
				Location destLoc = new Location(runeLoc.getWorld(),runeLoc.getBlockX()+.5, runeLoc.getBlockY(), runeLoc.getBlockZ()+.5);
				
//				Utils.loadChunkAtLocation(destLoc);
				
				/*//check for lapis below you
				if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
				   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot recall from a Lapis Lazuli block.");
					return;
				}*/
				
				//check for lapis below your target location
				if(destLoc.getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
				   destLoc.getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
					Output.simpleError(player, "Cannot recall to a Lapis Lazuli block.");
					return;
				}
				
				//Output.sendEffectTextNearbyExcludePlayer(player, "You hear a loud crack and the fizzle of electricity.");
				player.getWorld().strikeLightningEffect(player.getLocation());
				
//				pseudoPlayer._recentlyTeleportedTicks = 40;
				player.teleport(destLoc);
				
				player.getWorld().strikeLightningEffect(player.getLocation());
				
				//Output.sendEffectTextNearby(player, "You hear a loud crack and the fizzle of electricity.");
				if(usingSpawn) {
					pseudoPlayer.setMana(0);
					pseudoPlayer.setStamina(0);
					player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
				}
			}
			else Output.simpleError(player, "Cannot recall to there, the plot is private.");
		}
		else Output.simpleError(player, "You do not have a rune with that name, re-cast spell.");
		pseudoPlayer.getTimer().cantCastTicks = getCooldown();
	}
}
