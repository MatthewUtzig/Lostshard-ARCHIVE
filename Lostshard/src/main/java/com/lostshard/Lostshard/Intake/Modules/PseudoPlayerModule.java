package com.lostshard.Lostshard.Intake.Modules;

import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Intake.Providers.SenderPseudoPlayerProvider;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.sk89q.intake.parametric.AbstractModule;

public class PseudoPlayerModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(PseudoPlayer.class).annotatedWith(Sender.class).toProvider(new  SenderPseudoPlayerProvider());
	}

}
