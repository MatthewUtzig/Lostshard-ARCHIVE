package com.lostshard.CommandManager.Modules;

import org.bukkit.entity.Player;

import com.lostshard.CommandManager.Annotations.Sender;
import com.lostshard.CommandManager.Providers.PlayerProvider;
import com.lostshard.CommandManager.Providers.SenderPlayerProvider;
import com.sk89q.intake.parametric.AbstractModule;

public class PlayerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Player.class).annotatedWith(Sender.class).toProvider(new SenderPlayerProvider());
		bind(Player.class).toProvider(new PlayerProvider());
	}

}
