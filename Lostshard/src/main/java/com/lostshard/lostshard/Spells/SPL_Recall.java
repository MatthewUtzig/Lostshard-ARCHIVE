//package com.lostshard.lostshard.Spells;
//
//import java.util.ArrayList;
//
//import org.bukkit.ChatColor;
//import org.bukkit.Chunk;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.entity.Player;
//
//import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
//import com.lostshard.lostshard.Objects.PseudoPlayer;
//import com.lostshard.lostshard.Objects.Rune;
//import com.lostshard.lostshard.Objects.Runebook;
//import com.lostshard.lostshard.Utils.Output;
//
//public class SPL_Recall extends Spell {
//	private static final String 	_name = "Recall";
//	private static final String 	_spellWords = "Runus Teleporticus";
//	private static final int 		_castingDelay = 20;
//	private static final int 		_cooldownTicks = 20;
//	private static final int		_manaCost = 30;
//	private static final int[]		_reagentCost = {288};
//	private static final int 		_minMagery = 360;
//	
//	public String getName() 		{ return _name; }
//	public String getSpellWords() 	{ return _spellWords; }
//	public int getCastingDelay() 	{ return _castingDelay; }
//	public int getCooldownTicks()	{ return _cooldownTicks; }
//	public int getManaCost() 		{ return _manaCost; }
//	public int[] getReagentCost() 	{ return _reagentCost; }
//	public int getMinMagery() 		{ return _minMagery; }
//	
//	public int getPageNumber()		{ return 4; }
//	//public boolean isWandable()					 	{ return false; }
//	
//	String _response;
//	public String getPrompt() 						{ return "What rune would you like to recall from?"; }
//	public void setResponse(String response)		{ _response = response; }
//	
//	/* Used to confirm that the spell can be cast, so, for example, if you were
//	 * attempting to teleport to some location that was blocked, we would figure
//	 * that out here and cancel the spell.
//	 */
//	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {
//		return true;
//	}
//	
//	/* Used for anything that must be handled as soon as the spell is cast,
//	 * for example targeting a location for a delayed spell.
//	 */
//	public void preAction(Player player) {
//		Output.positiveMessage(player, "You begin casting Recall...");
//	}
//	
//	/* The meat of the spell code, this is what happens when the spell is
//	 * actually activated and should be doing something.
//	 */
//	public void doAction(Player player) {		
//		//System.out.println("RSPNS: "+_response);
//		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
//		
//		Runebook runebook = pseudoPlayer.getRunebook();
//		ArrayList<Rune> runes = runebook.getRunes();
//		Rune runeFound = null;
//		int count = 0;
//		for(Rune rune : runes) {
//			if(!player.isOp() && !pseudoPlayer.wasSubscribed() && (count >= 8))
//				break;
//			if(rune.getName().equalsIgnoreCase(_response)) {
//				runeFound = rune;
//				break;
//			}
//			count++;
//		}
//		
//		boolean usingSpawn = false;
//		if(runeFound == null) {
//				if(_response.equalsIgnoreCase("spawn")) {
//					usingSpawn = true;
////					if(pseudoPlayer.isCriminal())
////						runeFound = new Rune("", RPG._murdererSpawn, 0);
////					else 
////						runeFound = new Rune("", RPG._blueSpawn, 0);
//				}
//				else if(_response.equalsIgnoreCase("random")) {
////					if(player.getWorld().equals(RPG._normalWorld)) {
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
//		
//		if(runeFound != null) {
//			Location runeLoc = runeFound.getLocation();
//			if(!isValidRuneLocation(player, runeLoc)) {
//				return;
//			}
//			
//			Plot plot = PlotHandler.findPlotAt(runeLoc);
//			if((plot == null) || !plot.isLocked() || plot.isMember(player.getName())) {				
//				Location destLoc = new Location(runeLoc.getWorld(),runeLoc.getBlockX()+.5, runeLoc.getBlockY(), runeLoc.getBlockZ()+.5);
//				
//				Utils.loadChunkAtLocation(destLoc);
//				
//				/*//check for lapis below you
//				if(player.getLocation().getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
//				   player.getLocation().getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
//					Output.simpleError(player, "Cannot recall from a Lapis Lazuli block.");
//					return;
//				}*/
//				
//				//check for lapis below your target location
//				if(destLoc.getBlock().getRelative(0,-1,0).getType().equals(Material.LAPIS_BLOCK) ||
//				   destLoc.getBlock().getRelative(0,-2,0).getType().equals(Material.LAPIS_BLOCK)){
//					Output.simpleError(player, "Cannot recall to a Lapis Lazuli block.");
//					return;
//				}
//				
//				//Output.sendEffectTextNearbyExcludePlayer(player, "You hear a loud crack and the fizzle of electricity.");
//				player.getWorld().strikeLightningEffect(player.getLocation());
//				
//				pseudoPlayer._recentlyTeleportedTicks = 40;
//				player.teleport(destLoc);
//				
//				player.getWorld().strikeLightningEffect(player.getLocation());
//				
//				//Output.sendEffectTextNearby(player, "You hear a loud crack and the fizzle of electricity.");
//				if(usingSpawn) {
//					pseudoPlayer.setMana(0);
//					pseudoPlayer.setStamina(0);
//					player.sendMessage(ChatColor.GRAY+"Teleporting without a rune has exausted you.");
//				}
//			}
//			else Output.simpleError(player, "Cannot recall to there, the plot is private.");
//		}
//		else Output.simpleError(player, "You do not have a rune with that name, re-cast spell.");
//		pseudoPlayer.setCantCastTicks(_cooldownTicks);
//	}
//}
