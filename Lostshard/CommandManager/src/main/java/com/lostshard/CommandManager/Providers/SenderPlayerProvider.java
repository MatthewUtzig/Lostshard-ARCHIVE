package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class SenderPlayerProvider implements Provider<Player> {

	@Override
	public Player get(CommandArgs args, List<? extends Annotation> modifer) throws ArgumentException, ProvisionException {
		CommandSender sender = (CommandSender) args.getNamespace().get(CommandSender.class);
		if(sender instanceof Player)
			return (Player) sender;
		else
			throw new ProvisionException("Only players may perform this command");
	}

	@Override
	public List<String> getSuggestions(String arg0) {
		return ImmutableList.of();
	}

	@Override
	public boolean isProvided() {
		return true;
	}
	
	
}
