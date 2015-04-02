package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;

import com.lostshard.lostshard.Spells.MagicStructure;

public class WebTrap extends MagicStructure {

	public WebTrap(ArrayList<Block> blocks, UUID uuid,
			int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
	}

}
