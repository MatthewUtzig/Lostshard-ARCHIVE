package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;

import com.lostshard.lostshard.Database.Mappers.PermanentGateMapper;

public class PermanentGate extends Gate {

	private int id;

	public PermanentGate(ArrayList<Block> blocks, UUID uuid, boolean direction) {
		super(blocks, uuid, -1, direction);
		PermanentGateMapper.insertPermanentGate(this);
	}

	public PermanentGate(ArrayList<Block> blocks, UUID uuid, int id,
			boolean direction) {
		super(blocks, uuid, -1, direction);
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
}