package com.lostshard.Lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Spells.RangedSpell;
import com.lostshard.Lostshard.Spells.Scroll;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;
import com.lostshard.Plots.Models.Plot;
import com.lostshard.Plots.Models.Plot.PlotToggleable;

public class SPL_Light extends RangedSpell {

	public SPL_Light(Scroll scroll) {
		super(scroll);
		this.setRange(10);
		this.setCarePlot(true);
	}

	@Override
	public void doAction(Player player) {
		this.getFoundBlock().setType(Material.TORCH);
	}

	@Override
	public void preAction(Player player) {
	}

	@Override
	public boolean verifyCastable(Player player) {
		this.setFoundBlock(SpellUtils.faceBlockInLOS(player, this.getRange()));
		if (this.getFoundBlock() == null) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		if (!this.getFoundBlock().getType().equals(Material.AIR)) {
			Output.simpleError(player, "Invalid target.");
			return false;
		}
		final Plot plot = this.ptm.findPlotAt(this.getFoundBlock().getLocation());
		if (plot != null)
			if (plot.getToggleables().contains(PlotToggleable.PROTECTION))
				if (!plot.isAllowedToBuild(player)) {
					Output.simpleError(player, "You can't cast " + this.getName() + " there, that plot is protected.");
					return false;
				}
		return true;
	}

}
