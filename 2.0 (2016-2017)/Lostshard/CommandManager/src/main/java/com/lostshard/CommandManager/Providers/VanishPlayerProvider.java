package com.lostshard.CommandManager.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Main.Lostshard;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class VanishPlayerProvider implements Provider<Player> {

	@Override
	public boolean isProvided() {
		return false;
	}

	@Override
	public Player get(CommandArgs args, List<? extends Annotation> modifiers)
			throws ArgumentException, ProvisionException {
		String name = args.next();
		Player player = Bukkit.getPlayer(name);
		if(player != null && !Lostshard.isVanished(player))
			return player;
		else
			throw new ArgumentParseException("Theres no player with the name \""+name+"\" online.");
	}

	@Override
	public List<String> getSuggestions(String prefix) {
		return null;
	}

}
