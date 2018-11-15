package com.lostshard.RPG;

import com.lostshard.RPG.PseudoWolf;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Wolf;

public class PseudoWolfHandler {
	private static HashMap<String,PseudoWolf> _pseudoWolfHashMap = new HashMap<String,PseudoWolf>();
	private static ArrayList<PseudoWolf> _pseudoWolves = new ArrayList<PseudoWolf>();
	public static final boolean _debug = false;
	
	public static void tick(double delta) {
		// careful, new thread
		for(PseudoWolf pW : _pseudoWolves) {
			pW.tick(delta);
		}
	}
	
	public static void add(PseudoWolf pseudoWolf) {
		_pseudoWolfHashMap.put(pseudoWolf.getUniqueId(), pseudoWolf);
		_pseudoWolves.add(pseudoWolf);
	}
	
	public static void remove(String uniqueId) {
		_pseudoWolfHashMap.remove(uniqueId);
		for(int i=_pseudoWolves.size()-1; i>=0; i--) {
			if(_pseudoWolves.get(i).getUniqueId().equals(uniqueId)) {
				_pseudoWolves.remove(i);
			}
		}
	}
	
	public static PseudoWolf getPseudoWolf(String uniqueId) {
		if(_pseudoWolfHashMap.containsKey(uniqueId)) {
			PseudoWolf PseudoWolf = _pseudoWolfHashMap.get(uniqueId);
			return PseudoWolf;
		}
		return null;
	}
	
	public static PseudoWolf getPseudoWolf(Wolf wolf) {
		return getPseudoWolf(wolf.getUniqueId().toString());
	}
	
	public static ArrayList<PseudoWolf> getPseudoWolves() {
		return _pseudoWolves;
	}
	
	public static void updateHashMap(String oldUniqueId, String newUniqueId, PseudoWolf pseudoWolf) {
		_pseudoWolfHashMap.remove(oldUniqueId);
		_pseudoWolfHashMap.put(newUniqueId, pseudoWolf);
	}

}
