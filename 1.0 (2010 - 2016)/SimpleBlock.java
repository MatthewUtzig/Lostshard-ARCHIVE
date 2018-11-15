package com.lostshard.RPG;

import org.bukkit.World;

public class SimpleBlock {
	World _world;
	private int _typeId;
	private int _x;
	private int _y;
	private int _z;
	private String _serializedLocation;

	public SimpleBlock(World world, int typeId, int x, int y, int z) {
		_world = world;
		_typeId = typeId;
		_x = x;
		_y = y;
		_z = z;
		_serializedLocation = _world.getName()+_x+","+_y+","+_z;
	}
	
	public String getSerializedLocation() {
		return _serializedLocation;
	}
	
	public World getWorld() {
		return _world;
	}
	
	public int getTypeId() {
		return _typeId;
	}
	
	public int getX() {
		return _x;
	}
	
	public int getY() {
		return _y;
	}
	
	public int getZ() {
		return _z;
	}
}
