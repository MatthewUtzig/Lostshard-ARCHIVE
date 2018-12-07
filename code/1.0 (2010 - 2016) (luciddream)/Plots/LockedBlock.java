package com.lostshard.RPG.Plots;

public class LockedBlock {
	int _id;
	String _locString;
	int _key;
	
	public LockedBlock(int id, String locString, int key) {
		_id = id;
		_locString = locString;
		_key = key;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getLocString() {
		return _locString;
	}
	
	public int getKey() {
		return _key;
	}
}
