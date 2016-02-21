package com.lostshard.CommandManager.Modules;

import org.bukkit.command.CommandSender;

import com.lostshard.CommandManager.Providers.CommandSenderProvider;
import com.sk89q.intake.parametric.AbstractModule;

public class CommandSenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandSender.class).toProvider(new CommandSenderProvider());
	}

}
