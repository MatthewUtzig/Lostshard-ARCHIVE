package com.lostshard.Lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.bukkit.block.Block;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="permanentgates")
public class PermanentGate extends Gate {

	@Id
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment", strategy = "increment")
	private int id;

	public PermanentGate(ArrayList<Block> blocks, UUID uuid, boolean direction) {
		super(blocks, uuid, -1, direction);
		this.insert();
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