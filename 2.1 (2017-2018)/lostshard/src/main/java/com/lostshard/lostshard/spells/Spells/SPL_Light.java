package com.lostshard.lostshard.Spells.Spells;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.plot.Plot.PlotToggleable;
import com.lostshard.lostshard.Spells.RangedSpell;
import com.lostshard.lostshard.Spells.Scroll;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;

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
