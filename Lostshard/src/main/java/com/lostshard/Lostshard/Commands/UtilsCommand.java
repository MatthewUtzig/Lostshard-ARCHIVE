package com.lostshard.Lostshard.Commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Data.Locations;
import com.lostshard.Lostshard.Intake.Sender;
import com.lostshard.Lostshard.Intake.Vanish;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.Player.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;
import com.sk89q.intake.Command;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class UtilsCommand {

	PlayerManager pm = PlayerManager.getManager();
	
	@Command(aliases = { "build"}, desc = "Change your build", usage = "<build>")
	public void buildChange(@Sender Player player, @Sender PseudoPlayer pPlayer, @Range(max=2, min=0) int id) {
		if (!Utils.isWithin(player.getLocation(), Locations.BUILDCHANGLAWFULL.getLocation(), 5) && !Utils.isWithin(player.getLocation(), Locations.BUILDCHANGECRIMINAL.getLocation(), 5)) {
			Output.simpleError(player, "You need to be at the build change location to change build.");
			return;
		}
		if (!pPlayer.wasSubscribed() && id > 1) {
			Output.simpleError(player, "You may only use builds 0 and 1");
			return;
		}
		if (pPlayer.getBuilds().size() < id + 1) {
			final Build build = new Build();
			pPlayer.getBuilds().add(build);
		}
		pPlayer.setCurrentBuildId(id);
		Output.positiveMessage(player, "You have changed build to " + id + ".");
		player.getLocation().getWorld().strikeLightning(player.getLocation());
		player.setHealth(0);
	}

	@Command(aliases = { "ff"}, desc = "Toggles friendly fire")
	public void ff(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isFriendlyFire()) {
			Output.positiveMessage(player, "You have disabled friendly fire.");
			pPlayer.setFriendlyFire(false);
		} else {
			Output.positiveMessage(player, "You have enabled friendly fire.");
			pPlayer.setFriendlyFire(true);
		}
	}

	@Command(aliases = { "gui"}, desc = "Toggles gui")
	public void gui(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isAllowGui()) {
			Output.positiveMessage(player, "You have disabled inventory GUI.");
			pPlayer.setAllowGui(false);
		} else {
			Output.positiveMessage(player, "You have enabled inventory GUI.");
			pPlayer.setAllowGui(true);
		}
	}

	@Command(aliases = { "ignore"}, desc = "ignores a given player", usage="<player>")
	public void ignore(@Sender Player player, @Sender PseudoPlayer pPlayer, @Vanish @Optional Player tPlayer) {
		if (tPlayer == null) {
			Output.simpleError(player, "Use \"/ignore (player) \"");
			if (pPlayer.getIgnored().isEmpty())
				Output.simpleError(player, "You are currently not ignoring any players.");
			else
				player.sendMessage(
						ChatColor.YELLOW + Utils.listToString(Utils.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		if (player == tPlayer) {
			Output.simpleError(player, "You can't ignore your self.");
			return;
		}
		if (pPlayer.getIgnored().contains(tPlayer.getUniqueId())) {
			Output.simpleError(player, "You are currently ignoring " + tPlayer.getName() + ".");
			return;
		}
		Output.positiveMessage(player, "You are now ignoreing " + tPlayer.getName() + ".");
		pPlayer.getIgnored().add(tPlayer.getUniqueId());
	}

	@Command(aliases = { "kill"}, desc = "Kills your self")
	public void kill(@Sender Player player) {
		player.setHealth(0);
		Output.positiveMessage(player, "You have taken your own life.");
	}

	@Command(aliases = { "resetspawn"}, desc = "Resets your spawn location")
	public void playerResetSpawn(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isMurderer())
			Output.positiveMessage(player, "You have reset your spawn to Chaos.");
		else
			Output.positiveMessage(player, "You have reset your spawn to Order.");
		player.setBedSpawnLocation(null);
		return;
	}

	@Command(aliases = { "private"}, desc = "Sets you to private")
	public void playerSetPrivate(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.isPrivate())
			Output.positiveMessage(player, "You where already set private.");
		else {
			pPlayer.setPrivate(true);
			Output.positiveMessage(player, "You have been set to private.");
		}
	}

	@Command(aliases = { "public"}, desc = "Sets you to public")
	public void playerSetPublic(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (!pPlayer.isPrivate())
			Output.positiveMessage(player, "You where already set public.");
		else {
			pPlayer.setPrivate(false);
			Output.positiveMessage(player, "You have been set to public.");
		}
	}

	@Command(aliases = { "spawn"}, desc = "Spawn")
	public void playerSpawn(@Sender Player player, @Sender PseudoPlayer pPlayer) {
		if (pPlayer.getTimer().spawnTicks <= 0) {
			if (pPlayer.getTimer().goToSpawnTicks < 1) {
				pPlayer.getTimer().goToSpawnTicks = 100;
				Output.positiveMessage(player, "Returning to spawn in 10 seconds.");
			} else {
				Output.simpleError(player, "You are already spawning.");
			}
		} else {
			final int ticks = pPlayer.getTimer().spawnTicks;
			final int seconds = ticks / 10;
			Output.simpleError(player,
					"can't go to spawn, " + seconds / 60 + " minutes, " + seconds % 60 + " seconds remaining.");
		}
	}
	
	@Command(aliases = { "who", "playerlist", "list"}, desc = "List all online players")
	public void who(CommandSender sender) {
		Output.outputPlayerlist(sender);
	}

	@Command(aliases = { "whois"}, desc = "List all online players", usage = "<player>")
	public void whois(CommandSender sender, @Vanish Player target) {
		PseudoPlayer tpPlayer = pm.getPlayer(target);
		Output.outputWho(sender, tpPlayer);
	}
	
	@Command(aliases = { "stats"}, desc = "List all online players")
	public void stats(@Sender Player player) {
		Output.playerStats(player);
	}
	
	@Command(aliases = { "rules"}, desc = "List all online players")
	public void rules(CommandSender sender) {
		Output.displayRules(sender);
	}
	
	@Command(aliases = { "unignore"}, desc = "unignores player", usage="<player>")
	public void unignore(@Sender Player player, @Sender PseudoPlayer pPlayer, @Optional @Vanish Player target) {
		if (target == null) {
			Output.simpleError(player, "Use \"/unignore (player) \"");
			if (pPlayer.getIgnored().isEmpty())
				Output.simpleError(player, "You are currently not ignoring any players.");
			else
				player.sendMessage(
						ChatColor.YELLOW + Utils.listToString(Utils.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		if (!pPlayer.getIgnored().contains(target.getUniqueId())) {
			Output.simpleError(player, "You are currently not ignoring " + target.getName() + ".");
			return;
		}
		Output.positiveMessage(player, "You are no longer ignoreing " + target.getName() + ".");
		pPlayer.getIgnored().add(target.getUniqueId());
	}
}
