package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class SenderPseudoPlayerProvider implements Provider<PseudoPlayer> {

	@Override
	public PseudoPlayer get(CommandArgs args, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
		CommandSender sender = (CommandSender) args.getNamespace().get(CommandSender.class);
		if(sender instanceof Player)
			return PlayerManager.getManager().getPlayer((Player) sender);
		else
			throw new ProvisionException("Only players may perform this command.");
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
