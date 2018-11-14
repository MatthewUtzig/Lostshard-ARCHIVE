package com.lostshard.lostshard.Intake.Providers;

import java.lang.annotation.Annotation;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;
import com.lostshard.lostshard.Utils.TabUtils;
import com.sk89q.intake.argument.ArgumentException;
import com.sk89q.intake.argument.ArgumentParseException;
import com.sk89q.intake.argument.CommandArgs;
import com.sk89q.intake.parametric.Provider;
import com.sk89q.intake.parametric.ProvisionException;

public class PlayerProvider implements Provider<Player>{
	
	@Override
	public Player get(CommandArgs args, List<? extends Annotation> modifers) throws ArgumentException, ProvisionException {
		String name = args.next();
		Player player = Bukkit.getPlayer(name);
		if(player != null)
			return player;
		else
			throw new ArgumentParseException("Theres no player with the name \""+name+"\" online.");
	}

	@Override
	public List<String> getSuggestions(String arg) {
		Bukkit.broadcastMessage(arg);
		return ImmutableList.copyOf(TabUtils.OnlinePlayers(arg));
	}

	@Override
	public boolean isProvided() {
		return false;
	}

}
