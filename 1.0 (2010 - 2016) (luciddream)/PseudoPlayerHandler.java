package com.lostshard.RPG;

import com.lostshard.RPG.PseudoPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class PseudoPlayerHandler {
	private static HashMap<String,PseudoPlayer> _pseudoPlayerHashMap = new HashMap<String,PseudoPlayer>();
	private static ArrayList<PseudoPlayer> _pseudoPlayers = new ArrayList<PseudoPlayer>();
	public static final boolean _debug = false;
	
	public static void tick(double delta) {
		// careful, new thread
		for(PseudoPlayer pseudoPlayer : _pseudoPlayers) {
			pseudoPlayer.tick(delta);
		}
	}
	
	public static void add(String playerName, PseudoPlayer pseudoPlayer) {
		_pseudoPlayerHashMap.put(playerName, pseudoPlayer);
		_pseudoPlayers.add(pseudoPlayer);
		
		if(_debug)
			System.out.println("Added pseudoPlayer, total: " + _pseudoPlayers.size());
	}
	
	public static void removed(String playerName) {
		_pseudoPlayerHashMap.remove(playerName);
		for(int i=_pseudoPlayers.size()-1; i>=0; i--) {
			if(_pseudoPlayers.get(i).getName().equals(playerName)) {
				_pseudoPlayers.remove(i);
			}
		}
	}
	
	public static PseudoPlayer getPseudoPlayer(String playerName) {
		if(_pseudoPlayerHashMap.containsKey(playerName)) {
			PseudoPlayer pseudoPlayer = _pseudoPlayerHashMap.get(playerName);
			return pseudoPlayer;
		}
		return null;
	}
	
	public static ArrayList<PseudoPlayer> getPseudoPlayers() {
		return _pseudoPlayers;
	}

}
