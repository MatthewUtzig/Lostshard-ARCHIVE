package com.lostshard.lostshard.Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.lostshard.lostshard.Database.Database;
import com.lostshard.lostshard.Main.Lostshard;
import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;
import com.lostshard.lostshard.Skills.Build;
import com.lostshard.lostshard.Utils.Output;

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
		}
		return true;
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
		PseudoPlayer pPlayer = pm.getPlayer((Player) sender);
		if(args.length < 1) {
			if(pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build 0-2");
			else
				Output.simpleError(sender, "/build 0-1");
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
			Player player = (Player) sender;
			player.getLocation().getWorld().strikeLightning(player.getLocation());
		} catch (Exception e) {
			if(pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build (0|1|2)");
			else
				Output.simpleError(sender, "/build (0|1)");
			e.printStackTrace();
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
