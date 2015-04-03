package com.lostshard.lostshard.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Objects.Plot;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

public abstract class RangedSpell extends Spell {

	public RangedSpell(Scroll scroll) {
		super(scroll);
	}

	private int range;
	private Block foundBlock;
	private boolean carePlot;
	
	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public Block getFoundBlock() {
		return foundBlock;
	}

	public void setFoundBlock(Block foundBlock) {
		this.foundBlock = foundBlock;
	}

	public boolean isCarePlot() {
		return carePlot;
	}

	public void setCarePlot(boolean carePlot) {
		this.carePlot = carePlot;
	}
	
	public boolean verifyCastable(Player player) {
		setFoundBlock(SpellUtils.blockInLOS(player, getRange()));
		if(getFoundBlock() == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if(getFoundBlock().getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if(carePlot) {
			Plot plot = ptm.findPlotAt(getFoundBlock().getLocation());
			if(plot != null) {
				if(plot.isProtected()) {
					if(!plot.isFriendOrAbove(player)) {
						Output.simpleError(player, "You cannot cast "+getName()+" there, that plot is protected.");
						return false;
					}
				}
			}
		}
		return true;
	}
}
