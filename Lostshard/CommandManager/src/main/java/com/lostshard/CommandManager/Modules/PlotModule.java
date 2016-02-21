package com.lostshard.CommandManager.Modules;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Intake.Providers.NPCTypeProvider;
import com.lostshard.Lostshard.Intake.Providers.PlotEffectProvider;
import com.lostshard.Lostshard.Intake.Providers.PlotProvider;
import com.lostshard.Lostshard.Intake.Providers.PlotUpgradeProvider;
import com.lostshard.Lostshard.Intake.Providers.SenderPlotProvider;
import com.lostshard.Lostshard.NPC.NPCType;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotEffect;
import com.lostshard.Lostshard.Objects.Plot.Plot.PlotUpgrade;
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
