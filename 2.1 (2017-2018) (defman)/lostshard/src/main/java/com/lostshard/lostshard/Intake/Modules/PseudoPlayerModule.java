package com.lostshard.lostshard.Intake.Modules;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Intake.Providers.SenderPseudoPlayerProvider;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;
import com.sk89q.intake.parametric.AbstractModule;

public class PseudoPlayerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PseudoPlayer.class).annotatedWith(Sender.class).toProvider(new  SenderPseudoPlayerProvider());
	}

}
