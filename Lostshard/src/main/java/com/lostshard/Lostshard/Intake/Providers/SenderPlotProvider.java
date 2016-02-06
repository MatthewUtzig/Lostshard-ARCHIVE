package com.lostshard.Lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.lostshard.Lostshard.Manager.PlotManager;
import com.lostshard.Lostshard.Objects.Plot.Plot;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class SenderPlotProvider implements Provider<Plot> {

	@Override
	public Plot get(CommandArgs args, List<? extends Annotation> modifers) throws ArgumentException, ProvisionException {
		CommandSender sender = (CommandSender) args.getNamespace().get(CommandSender.class);
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			Plot plot = PlotManager.getManager().findPlotAt(player.getLocation());
			
			if(plot != null)
				return plot;
			throw new ProvisionException("You are not currently in a plot.");
		} else
			throw new ProvisionException("Only players may perform this command.");
	}

	@Override
	public List<String> getSuggestions(String arg) {
		return ImmutableList.of();
	}

	@Override
	public boolean isProvided() {
		return true;
	}

}
