package com.lostshard.Lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class PlotProvider implements Provider<Plot> {

	@Override
	public Plot get(CommandArgs args, List<? extends Annotation> modifer) throws ArgumentException, ProvisionException {
		String name = args.next();
		Plot plot = PlotManager.getManager().getPlot(name);
		if(plot != null)
			return plot;
		else
			throw new ProvisionException("Theres no plot with the name \""+name+"\".");
	}

	@Override
	public List<String> getSuggestions(String arg) {
		return TabUtils.Plots();
	}

	@Override
	public boolean isProvided() {
		return false;
	}
}
