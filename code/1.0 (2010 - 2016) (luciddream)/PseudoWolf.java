package com.lostshard.RPG;

public class PseudoWolf {
	private int _id;
	private String _name;
	private String _uniqueId;
	
	public PseudoWolf(String uniqueId, String name) {
		_name = name;
		_uniqueId = uniqueId;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getUniqueId() {
		return _uniqueId;
	}
	
	public void setUniqueId(String uniqueId) {
		_uniqueId = uniqueId;
	}
	
	public void tick(double delta) {
		
	}
	
	public String getName() {
		return _name;
	}
}
