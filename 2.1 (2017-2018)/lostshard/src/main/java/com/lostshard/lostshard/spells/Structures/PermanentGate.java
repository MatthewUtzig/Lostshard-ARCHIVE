package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.bukkit.block.Block;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Access(AccessType.FIELD)
public class PermanentGate extends Gate {

	@Id
	@GeneratedValue(generator = "increment")
	@GenericGenerator(name = "increment", strategy = "increment")
	private int id;
	
	public PermanentGate() {
		super();
	}
	
	public PermanentGate(int id, List<Block> blocks, UUID uuid, boolean direction) {
		super(blocks, uuid, -1, direction);
		this.id = id;
	}
	
	public PermanentGate(ArrayList<Block> blocks, UUID uuid, boolean direction) {
		super(blocks, uuid, -1, direction);
		this.insert();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
}