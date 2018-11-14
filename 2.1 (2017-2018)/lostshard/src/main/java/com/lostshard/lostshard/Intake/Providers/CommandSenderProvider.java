package com.lostshard.lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class CommandSenderProvider implements Provider<CommandSender> {

	@Override
	public CommandSender get(CommandArgs args, List<? extends Annotation> modifers)
			throws ArgumentException, ProvisionException {
		return args.getNamespace().get(CommandSender.class);
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
