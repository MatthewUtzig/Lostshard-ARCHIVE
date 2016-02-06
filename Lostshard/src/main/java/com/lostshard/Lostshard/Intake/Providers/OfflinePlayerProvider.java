package com.lostshard.Lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class OfflinePlayerProvider implements Provider<OfflinePlayer>{


	@Override
	public boolean isProvided() {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public OfflinePlayer get(CommandArgs arguments, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
		return Bukkit.getOfflinePlayer(arguments.next());
	}

	@Override
	public List<String> getSuggestions(String prefix) {
		return ImmutableList.of();
	}

}
