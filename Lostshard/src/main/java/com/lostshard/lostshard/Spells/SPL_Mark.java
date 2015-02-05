package com.lostshard.lostshard.Spells;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Handlers.PlotHandler;
import com.lostshard.lostshard.Handlers.PseudoPlayerHandler;
import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Objects.Rune;
import com.lostshard.lostshard.Objects.Runebook;
import com.lostshard.lostshard.Utils.Output;

public class SPL_Mark extends Spell {
	private static final String 	_name = "Mark";
	private static final String 	_spellWords = "Runus Markius";
	private static final int 		_castingDelay = 10;
	private static final int 		_cooldownTicks = 10;
	private static final int		_manaCost = 20;
	private static final int[]		_reagentCost = {288,331};
	private static final int 		_minMagery = 360;
	
	public String getName() 		{ return _name; }
	public String getSpellWords() 	{ return _spellWords; }
	public int getCastingDelay() 	{ return _castingDelay; }
	public int getCooldownTicks()	{ return _cooldownTicks; }
	public int getManaCost() 		{ return _manaCost; }
	public int[] getReagentCost() 	{ return _reagentCost; }
	public int getMinMagery() 		{ return _minMagery; }
	
	public int getPageNumber()		{ return 4; }
	//public boolean isWandable()					 	{ return false; }
	
	String _response;
	public String getPrompt() 						{ return "What would you like to label the marked rune?"; }
	public void setResponse(String response)		{ _response = response; }
	
	private Location _markLocation;
	/* Used to confirm that the spell can be cast, so, for example, if you were
	 * attempting to teleport to some location that was blocked, we would figure
	 * that out here and cancel the spell.
	 */
	public boolean verifyCastable(Player player, PseudoPlayer pseudoPlayer) {	
		Plot plot = PlotHandler.findPlotAt(player.getLocation());
		if((plot == null) || !plot.isPrivatePlot() || plot.isFriendOrAbove(player)) {
			_markLocation = player.getLocation();
		}
		else {
			Output.simpleError(player, "You cannot mark a rune here, the plot is private.");
			return false;
		}
		
		if(!isValidRuneLocation(player, player.getLocation())) {
			return false;
		}
		
		return true;
	}
	
	/* Used for anything that must be handled as soon as the spell is cast,
	 * for example targeting a location for a delayed spell.
	 */
	public void preAction(Player player) {
		Output.positiveMessage(player, "You begin casting Mark...");
	}
	
	/* The meat of the spell code, this is what happens when the spell is
	 * actually activated and should be doing something.
	 */
	public void doAction(Player player) {
		//System.out.println("RSPNS: "+_response);
		PseudoPlayer pseudoPlayer = PseudoPlayerHandler.getPlayer(player);
		if(_response.length() > 20 || _response.contains("\"") || _response.contains("'")) {
			Output.simpleError(player, "Invalid characters or too long, 20 char max.");
		}
		else {
			Runebook runebook = pseudoPlayer.getRunebook();
			int numRunes = runebook.getNumRunes();
			if(player.isOp() || (pseudoPlayer.wasSubscribed() && numRunes < 32) || numRunes < 8) {
				ArrayList<Rune> runes = runebook.getRunes();
				boolean foundMatching = false;
				for(Rune rune : runes) {
					if(rune.getName().equalsIgnoreCase(_response)) {
						foundMatching = true;
						break;
					}
				}
				if(!foundMatching) {
//					int runeId = Database.addRune(pseudoPlayer.getId(), player.getName(), _response, _markLocation);
					Rune newRune = new Rune(_markLocation, _response, 0);
					runebook.addRune(newRune);
					Output.positiveMessage(player, "You have marked a rune for "+_response+".");
				}
				else Output.simpleError(player, "You already have a rune with that name, re-cast the spell.");
			}
			else Output.simpleError(player, "Too many runes, remove one to mark a new rune.");
		}
		pseudoPlayer.setCantCastTicks(_cooldownTicks);
	}
}
