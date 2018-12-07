package com.lostshard.RPG.Plots;

import me.neodork.npclib.entity.HumanNPC;

import org.bukkit.Location;

public class RPGNPC {
	private String _uniqueId;
	private String _name;
	private int _itemInHandId;
	private String _job;
	private Location _location;
	private HumanNPC _npcEntity;
	
	public RPGNPC(String uniqueId, String name, int itemInHandId, String job, Location location, HumanNPC npcEntity) {
		_uniqueId = uniqueId;
		_name = name;
		_itemInHandId = itemInHandId;
		_job = job;
		_location = location;
		_npcEntity = npcEntity;
	}
	
	public String getUniqueId() {
		return _uniqueId;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getItemInHandId() {
		return _itemInHandId;
	}
	
	public String getJob() {
		return _job;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public void setLocation(Location location) {
		_location = location;
	}
	
	public HumanNPC getNPCEntity() {
		return _npcEntity;
	}
	
	public void setNPCEntity(HumanNPC npcEntity) {
		_npcEntity = npcEntity;
	}
}
