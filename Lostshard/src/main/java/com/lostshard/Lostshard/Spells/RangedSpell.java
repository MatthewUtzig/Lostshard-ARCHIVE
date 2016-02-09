package com.lostshard.Lostshard.Spells;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotToggleable;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public abstract class RangedSpell extends Spell {

	private int range;

	private Block foundBlock;
	private boolean carePlot;

	public RangedSpell(Scroll scroll) {
		super(scroll);
	}

	public Block getFoundBlock() {
		return this.foundBlock;
	}

	public int getRange() {
		return this.range;
	}

	public boolean isCarePlot() {
		return this.carePlot;
	}

	public void setCarePlot(boolean carePlot) {
		this.carePlot = carePlot;
	}

	public void setFoundBlock(Block foundBlock) {
		this.foundBlock = foundBlock;
	}

	public void setRange(int range) {
		this.range = range;
	}

	@Override
	public boolean verifyCastable(Player player) {
		this.setFoundBlock(SpellUtils.blockInLOS(player, this.getRange()));
		if (this.getFoundBlock() == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if (this.getFoundBlock().getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if (this.carePlot) {
			final Plot plot = this.ptm.findPlotAt(this.getFoundBlock().getLocation());
			if (plot != null)
				if (plot.getToggleables().contains(PlotToggleable.PROTECTION))
					if (!plot.isFriendOrAbove(player)) {
						Output.simpleError(player,
								"You can't cast " + this.getName() + " there, that plot is protected.");
						return false;
					}
		}
		return true;
	}
}
