package com.lostshard.lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.Utils.TabUtils;
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
