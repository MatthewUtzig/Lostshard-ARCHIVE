package com.lostshard.lostshard.Commands;

import java.util.List;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Handlers.HelpHandler;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Locations;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.Utils;

public class UtilsCommand implements CommandExecutor, TabCompleter {

	PlayerManager pm = PlayerManager.getManager();
	
	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public UtilsCommand(Lostshard plugin) {
		plugin.getCommand("stats").setExecutor(this);
		plugin.getCommand("who").setExecutor(this);
		plugin.getCommand("whois").setExecutor(this);
		plugin.getCommand("spawn").setExecutor(this);
		plugin.getCommand("resetspawn").setExecutor(this);
		plugin.getCommand("rules").setExecutor(this);
		plugin.getCommand("build").setExecutor(this);
		plugin.getCommand("private").setExecutor(this);
		plugin.getCommand("public").setExecutor(this);
		plugin.getCommand("kill").setExecutor(this);
		plugin.getCommand("gui").setExecutor(this);
		plugin.getCommand("ff").setExecutor(this);
		plugin.getCommand("ignore").setExecutor(this);
		plugin.getCommand("unignore").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("stats")) {
			Output.displayStats(sender);
		} else if (cmd.getName().equalsIgnoreCase("who")) {
			Output.outputPlayerlist(sender);
		} else if (cmd.getName().equalsIgnoreCase("whois")) {
			Output.displayWho(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("spawn")) {
			playerSpawn(sender);
		} else if (cmd.getName().equalsIgnoreCase("resetspawn")) {
			playerResetSpawn(sender);
		} else if (cmd.getName().equalsIgnoreCase("rules")) {
			Output.displayRules(sender);
		} else if (cmd.getName().equalsIgnoreCase("build")) {
			buildChange(sender, args);
		} else if (cmd.getName().equalsIgnoreCase("private")) {
			playerSetPrivate(sender);
		} else if (cmd.getName().equalsIgnoreCase("public")) {
			playerSetPublic(sender);
		} else if (cmd.getName().equalsIgnoreCase("kill")) {
			kill(sender);
		} else if(cmd.getName().equalsIgnoreCase("gui")) {
			gui(sender);
		} else if(cmd.getName().equalsIgnoreCase("ff")) {
			ff(sender);
		} else if(cmd.getName().equalsIgnoreCase("ignore")) {
			ignore(sender, args);
		} else if(cmd.getName().equalsIgnoreCase("unignore")) {
			unignore(sender, args);
		} else if(cmd.getName().equalsIgnoreCase("help")) {
			HelpHandler.handle(sender, args);
		}
		return true;
	}

	private void ignore(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(args.length < 1) {
			Output.simpleError(player, "Use \"/ignore (player) \"");
			if(pPlayer.getIgnored().isEmpty())
				Output.simpleError(player, "You are currently not ignoring any players.");
			else
				player.sendMessage(ChatColor.YELLOW+Utils.listToString(Utils.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		Player tPlayer = Bukkit.getPlayer(args[0]);
		if(tPlayer == null) {
			Output.simpleError(player, args[0]+" is not online.");
			return;
		}
		if(pPlayer.getIgnored().contains(tPlayer.getUniqueId())) {
			Output.simpleError(player, "You are currently ignoring "+tPlayer.getName()+".");
			return;
		}
		Output.positiveMessage(player, "You are now ignoreing "+tPlayer.getName()+".");
		pPlayer.getIgnored().add(tPlayer.getUniqueId());
	}

	private void unignore(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(args.length < 1) {
			Output.simpleError(player, "Use \"/unignore (player) \"");
			if(pPlayer.getIgnored().isEmpty())
				Output.simpleError(player, "You are currently not ignoring any players.");
			else
				player.sendMessage(ChatColor.YELLOW+Utils.listToString(Utils.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		@SuppressWarnings("deprecation")
		OfflinePlayer tPlayer = Bukkit.getOfflinePlayer(args[0]);
		if(!pPlayer.getIgnored().contains(tPlayer.getUniqueId())) {
			Output.simpleError(player, "You are currently not ignoring "+tPlayer.getName()+".");
			return;
		}
		Output.positiveMessage(player, "You are no longer ignoreing "+tPlayer.getName()+".");
		pPlayer.getIgnored().add(tPlayer.getUniqueId());
	}

	private void ff(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if(pPlayer.isFriendlyFire()) {
			Output.positiveMessage(sender, "You have disabled friendly fire.");
			pPlayer.setFriendlyFire(false);
		} else {
			Output.positiveMessage(sender, "You have enabled friendly fire.");
			pPlayer.setFriendlyFire(true);
		}
	}

	private void gui(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if(pPlayer.isAllowGui()) {
			Output.positiveMessage(sender, "You have disabled inventory GUI.");
			pPlayer.setAllowGui(false);
		} else {
			Output.positiveMessage(sender, "You have enabled inventory GUI.");
			pPlayer.setAllowGui(true);
		}
	}

	private void kill(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		player.setHealth(0);
		Output.positiveMessage(sender, "You have taken your own life.");
	}

	private void playerSetPublic(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if(!pPlayer.isPrivate())
			Output.positiveMessage(sender, "You where already set public.");
		else {
			pPlayer.setPrivate(false);
			Output.positiveMessage(sender, "You have been set to public.");
		}
	}

	private void playerSetPrivate(CommandSender sender) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if(pPlayer.isPrivate())
			Output.positiveMessage(sender, "You where already set private.");
		else {
			pPlayer.setPrivate(true);
			Output.positiveMessage(sender, "You have been set to private.");
		}
	}

	private void buildChange(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		Player player = (Player) sender;
		PseudoPlayer pPlayer = pm.getPlayer(player);
		if(args.length < 1) {
			if(pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build 0-2");
			else
				Output.simpleError(sender, "/build 0-1");
			return;
		}
		if(!Utils.isWithin(player.getLocation(), Locations.BUILDCHANGLAWFULL.getLocation(), 5)) {
			Output.simpleError(player, "You need to be at the build change location to change build.");
			return;
		}
		try {
			int id = Integer.parseInt(args[0]);
    		if(id < 0) {
    			Output.simpleError(sender, "Invalid build number.");
    			return;
    		}
    		
    		if(pPlayer.wasSubscribed()) {
    			if(id > 2) {
    				Output.simpleError(sender, "You may only use builds 0, 1, and 2.");
    				return;
    			}
    		}
    		else if(id > 1) {
    			Output.simpleError(sender, "You may only use builds 0 and 1");
    			return;
    		}
			if(pPlayer.getBuilds().size() < id+1) {
				Build build = new Build();
				pPlayer.getBuilds().add(build);
				Database.insertBuild(build, pPlayer.getId());
			}
			pPlayer.setCurrentBuildId(id);
			Output.positiveMessage(sender, "You have changed build to "+id+".");
			player.getLocation().getWorld().strikeLightning(player.getLocation());
			player.setHealth(0);
		} catch (Exception e) {
			if(pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build (0|1|2)");
			else
				Output.simpleError(sender, "/build (0|1)");
		}
	}

	private void playerResetSpawn(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if (pPlayer.isMurderer())
			Output.positiveMessage(sender,
					"You have reset your spawn to Chaos.");
		else
			Output.positiveMessage(sender,
					"You have reset your spawn to Order.");
		((Player)sender).setBedSpawnLocation(null);
		return;
	}

	private void playerSpawn(CommandSender sender) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			PseudoPlayer pPlayer = pm.getPlayer(player);
        	if(pPlayer.getTimer().goToSpawnTicks <= 0) {
        		pPlayer.getTimer().goToSpawnTicks = 100;
        		Output.positiveMessage(player, "Returning to spawn in 10 seconds.");
        	}
        	else {
        		int ticks = pPlayer.getTimer().spawnTicks;
        		int seconds = ticks / 10;
        		player.sendMessage("Cannot go to spawn, "+(seconds/60)+" minutes, "+(seconds%60)+" seconds remaining.");
        	}
		}else{
			Output.mustBePlayer(sender);
		}
	}

	public List<String> onTabComplete(CommandSender sender, Command cmd,
			String string, String[] args) {
		if(cmd.getName().equalsIgnoreCase("private") || cmd.getName().equalsIgnoreCase("public"))
			return null;
		return null;
	}
}
