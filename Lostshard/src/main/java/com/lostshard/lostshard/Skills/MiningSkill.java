package com.lostshard.lostshard.Skills;

import org.bukkit.event.block.BlockBreakEvent;

public class MiningSkill extends Skill {

	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled())
			return;
	}
	
}
