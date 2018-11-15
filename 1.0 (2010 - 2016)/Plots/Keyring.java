package com.lostshard.RPG.Plots;

import java.util.HashSet;

public class Keyring {
	HashSet<Integer> _keys;
	
	public Keyring(HashSet<Integer> keys) {
		_keys = keys;
	}
	
	public boolean hasKey(Integer key) {
		if(_keys.contains(key))
			return true;
		return false;
	}
	
	public void addKey(int key) {
		_keys.add(key);
	}
	
	public void removeKey(int key) {
		_keys.remove(key);
	}
}
