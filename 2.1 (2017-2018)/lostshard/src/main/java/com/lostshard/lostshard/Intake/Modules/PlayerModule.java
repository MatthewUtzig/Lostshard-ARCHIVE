package com.lostshard.lostshard.Intake.Modules;

import org.bukkit.entity.Player;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Intake.Providers.PlayerProvider;
import com.lostshard.lostshard.Intake.Providers.SenderPlayerProvider;
import com.sk89q.intake.parametric.AbstractModule;

public class PlayerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Player.class).annotatedWith(Sender.class).toProvider(new SenderPlayerProvider());
		bind(Player.class).toProvider(new PlayerProvider());
	}

}
