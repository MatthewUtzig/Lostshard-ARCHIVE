package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Utils.TabUtils;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class PseudoPlayerProvider implements Provider<PseudoPlayer> {

	@Override
	public PseudoPlayer get(CommandArgs args, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
//		String name = args.next();
//		Player player = Bukkit.getPlayer(name);
		return null;
	}

	@Override
	public List<String> getSuggestions(String arg) {
		return TabUtils.OnlinePlayers(arg);
	}

	@Override
	public boolean isProvided() {
		return false;
	}

}
