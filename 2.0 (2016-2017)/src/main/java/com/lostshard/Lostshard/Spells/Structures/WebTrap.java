package com.lostshard.Lostshard.Spells.Structures;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.block.Block;

import com.lostshard.Lostshard.Spells.MagicStructure;

public class WebTrap extends MagicStructure {

	public WebTrap(ArrayList<Block> blocks, UUID uuid, int numTicksTillCleanup) {
		super(blocks, uuid, numTicksTillCleanup);
	}

}
