package com.lostshard.lostshard.Intake;

import org.bukkit.command.CommandSender;

import com.lostshard.lostshard.Intake.Providers.CommandSenderProvider;
import com.sk89q.intake.parametric.AbstractModule;

public class CommandSenderModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(CommandSender.class).toProvider(new CommandSenderProvider());
	}

}
