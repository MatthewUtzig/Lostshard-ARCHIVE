package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;

import com.lostshard.lostshard.Database.Database;

public class PermanentGate extends Gate {

	public PermanentGate(ArrayList<Block> blocks, UUID uuid, int id) {
		super(blocks, uuid, -1);
		this.id = id;
	}
	
	public PermanentGate(ArrayList<Block> blocks, UUID uuid) {
		super(blocks, uuid, -1);
		Database.insertPermanentGate(this);
	}
	
	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}