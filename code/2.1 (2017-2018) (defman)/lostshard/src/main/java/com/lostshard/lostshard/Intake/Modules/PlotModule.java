package com.lostshard.lostshard.Intake.Modules;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Intake.Providers.NPCTypeProvider;
import com.lostshard.lostshard.Intake.Providers.PlotEffectProvider;
import com.lostshard.lostshard.Intake.Providers.PlotProvider;
import com.lostshard.lostshard.Intake.Providers.PlotUpgradeProvider;
import com.lostshard.lostshard.Intake.Providers.SenderPlotProvider;
import com.lostshard.lostshard.NPC.NPCType;
import com.lostshard.lostshard.plot.Plot;
import com.lostshard.lostshard.plot.Plot.PlotEffect;
import com.lostshard.lostshard.plot.Plot.PlotUpgrade;
import com.sk89q.intake.parametric.AbstractModule;

public class PlotModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Plot.class).annotatedWith(Sender.class).toProvider(new SenderPlotProvider());
		bind(Plot.class).toProvider(new PlotProvider());
		bind(PlotUpgrade.class).toProvider(new PlotUpgradeProvider());
		bind(PlotEffect.class).toProvider(new PlotEffectProvider());
		bind(NPCType.class).toProvider(new NPCTypeProvider());
	}

}
