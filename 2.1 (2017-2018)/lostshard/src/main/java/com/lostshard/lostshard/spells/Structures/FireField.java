package com.lostshard.lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.lostshard.lostshard.Spells.MagicStructure;

public class FireField extends MagicStructure {

	public FireField(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
		for (final Block block : blocks)
			block.setType(Material.FIRE);
		this.setBlocks(blocks);
	}

}
