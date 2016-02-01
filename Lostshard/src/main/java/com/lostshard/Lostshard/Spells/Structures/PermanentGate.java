package com.lostshard.Lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bukkit.block.Block;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class PermanentGate extends Gate {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	public PermanentGate() {
		super(new ArrayList<Block>(), null, -1, false);
	}
	
	public PermanentGate(ArrayList<Block> blocks, UUID uuid, boolean direction) {
		super(blocks, uuid, -1, direction);
		this.insert();
	}

	public PermanentGate(ArrayList<Block> blocks, UUID uuid, int id, boolean direction) {
		super(blocks, uuid, -1, direction);
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
}