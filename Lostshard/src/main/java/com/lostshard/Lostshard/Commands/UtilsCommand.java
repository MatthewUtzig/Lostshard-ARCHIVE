package com.lostshard.Lostshard.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Data.Locations;
import com.lostshard.Lostshard.Database.Mappers.BuildMapper;
import com.lostshard.Lostshard.Handlers.HelpHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;
import com.lostshard.Lostshard.Skills.Build;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.Utils;

public class UtilsCommand extends LostshardCommand {

	PlayerManager pm = PlayerManager.getManager();

	/**
	 * @param Lostshard
	 *            as plugin
	 */
	public UtilsCommand(Lostshard plugin) {
		super(plugin, "stats", "who", "whois", "spawn", "resetspawn", "rules", 
				"build", "private", "public", "kill", "gui", "ff", "ignore",
				"unignore", "help", "title");
	}

	private void buildChange(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			if (pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build 0-2");
			else
				Output.simpleError(sender, "/build 0-1");
			return;
		}
		if (!Utils.isWithin(player.getLocation(),
				Locations.BUILDCHANGLAWFULL.getLocation(), 5)) {
			Output.simpleError(player,
					"You need to be at the build change location to change build.");
			return;
		}
		try {
			final int id = Integer.parseInt(args[0]);
			if (id < 0) {
				Output.simpleError(sender, "Invalid build number.");
				return;
			}

			if (pPlayer.wasSubscribed()) {
				if (id > 2) {
					Output.simpleError(sender,
							"You may only use builds 0, 1, and 2.");
					return;
				}
			} else if (id > 1) {
				Output.simpleError(sender, "You may only use builds 0 and 1");
				return;
			}
			if (pPlayer.getBuilds().size() < id + 1) {
				final Build build = new Build(0);
				pPlayer.getBuilds().add(build);
				BuildMapper.insertBuild(build, pPlayer.getId());
			}
			pPlayer.setCurrentBuildId(id);
			Output.positiveMessage(sender, "You have changed build to " + id
					+ ".");
			player.getLocation().getWorld()
					.strikeLightning(player.getLocation());
			player.setHealth(0);
		} catch (final Exception e) {
			if (pPlayer.wasSubscribed())
				Output.simpleError(sender, "/build (0|1|2)");
			else
				Output.simpleError(sender, "/build (0|1)");
		}
	}

	private void ff(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer((Player) sender);
		if (pPlayer.isFriendlyFire()) {
			Output.positiveMessage(sender, "You have disabled friendly fire.");
			pPlayer.setFriendlyFire(false);
		} else {
			Output.positiveMessage(sender, "You have enabled friendly fire.");
			pPlayer.setFriendlyFire(true);
		}
	}

	private void gui(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer((Player) sender);
		if (pPlayer.isAllowGui()) {
			Output.positiveMessage(sender, "You have disabled inventory GUI.");
			pPlayer.setAllowGui(false);
		} else {
			Output.positiveMessage(sender, "You have enabled inventory GUI.");
			pPlayer.setAllowGui(true);
		}
	}

	private void ignore(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			Output.simpleError(player, "Use \"/ignore (player) \"");
			if (pPlayer.getIgnored().isEmpty())
				Output.simpleError(player,
						"You are currently not ignoring any players.");
			else
				player.sendMessage(ChatColor.YELLOW
						+ Utils.listToString(Utils
								.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		final Player tPlayer = Bukkit.getPlayer(args[0]);
		if (tPlayer == null) {
			Output.simpleError(player, args[0] + " is not online.");
			return;
		}
		if (player == tPlayer) {
			Output.simpleError(player, "You can't ignore your self.");
			return;
		}
		if (pPlayer.getIgnored().contains(tPlayer.getUniqueId())) {
			Output.simpleError(player,
					"You are currently ignoring " + tPlayer.getName() + ".");
			return;
		}
		Output.positiveMessage(player,
				"You are now ignoreing " + tPlayer.getName() + ".");
		pPlayer.getIgnored().add(tPlayer.getUniqueId());
	}

	private void kill(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		player.setHealth(0);
		Output.positiveMessage(sender, "You have taken your own life.");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string,
			String[] args) {
		if (cmd.getName().equalsIgnoreCase("stats"))
			Output.displayStats(sender);
		else if (cmd.getName().equalsIgnoreCase("who"))
			Output.outputPlayerlist(sender);
		else if (cmd.getName().equalsIgnoreCase("whois"))
			Output.displayWho(sender, args);
		else if (cmd.getName().equalsIgnoreCase("spawn"))
			this.playerSpawn(sender);
		else if (cmd.getName().equalsIgnoreCase("resetspawn"))
			this.playerResetSpawn(sender);
		else if (cmd.getName().equalsIgnoreCase("rules"))
			Output.displayRules(sender);
		else if (cmd.getName().equalsIgnoreCase("build"))
			this.buildChange(sender, args);
		else if (cmd.getName().equalsIgnoreCase("private"))
			this.playerSetPrivate(sender);
		else if (cmd.getName().equalsIgnoreCase("public"))
			this.playerSetPublic(sender);
		else if (cmd.getName().equalsIgnoreCase("kill"))
			this.kill(sender);
		else if (cmd.getName().equalsIgnoreCase("gui"))
			this.gui(sender);
		else if (cmd.getName().equalsIgnoreCase("ff"))
			this.ff(sender);
		else if (cmd.getName().equalsIgnoreCase("ignore"))
			this.ignore(sender, args);
		else if (cmd.getName().equalsIgnoreCase("unignore"))
			this.unignore(sender, args);
		else if (cmd.getName().equalsIgnoreCase("help"))
			HelpHandler.handle(sender, args);
		else if (cmd.getName().equalsIgnoreCase("title"))
			this.title(sender, args);
		return true;
	}

	private void title(CommandSender sender, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED+"Only players may perform this command.");
			return;
		}
		Player player = (Player) sender;
		if(args.length > 0) {
			String subCmd = args[0];
			if(subCmd.equalsIgnoreCase("none") || subCmd.equalsIgnoreCase("null")) {
				PseudoPlayer pPlayer = pm.getPlayer(player);
				pPlayer.setCurrentTitleId(-1);
				Output.positiveMessage(player, "You have disabled your title");
			}else if(subCmd.equalsIgnoreCase("give")) {
				if(!(args.length > 2)) {
					Output.simpleError(player, "/title give (player) (title)");
					return;
				}
				String name = args[1];
				Player tPlayer = Utils.getPlayer(name);
				if(tPlayer == null) {
					Output.simpleError(player, name+" is not online.");
					return;
				}
				PseudoPlayer pPlayer = pm.getPlayer(tPlayer);
				String title = StringUtils.join(args, " ", 2, args.length);
				if(title.length() > 15) {
					Output.simpleError(player, "Title must be less than 15 characters.");
					return;
				}
				pPlayer.getTitels().add(title);
				Output.positiveMessage(player, "You have given \""+title+"\" to \""+tPlayer.getName()+"\".");
			}else if(subCmd.equalsIgnoreCase("remove") || subCmd.equalsIgnoreCase("take")) {
				if(args.length < 2) {
					Output.simpleError(player, "/title take (player) (title)");
					return;
				}
				String name = args[1];
				Player tPlayer = Utils.getPlayer(name);
				if(tPlayer == null) {
					Output.simpleError(player, name+" is not online.");
					return;
				}
				PseudoPlayer pPlayer = pm.getPlayer(tPlayer);
				String title = StringUtils.join(args, " ", 2, args.length);
				if(pPlayer.getTitels().remove(title))
					Output.positiveMessage(player, "You have taken \""+title+"\" from \""+tPlayer.getName()+"\".");
				else
					Output.simpleError(player, tPlayer.getName()+" do not have such title.");
			}else{
				PseudoPlayer pPlayer = pm.getPlayer(player);
				subCmd = StringUtils.join(args);
				for(int i=0; i<pPlayer.getTitels().size(); i++) {
					String title = pPlayer.getTitels().get(i);
					if(StringUtils.containsIgnoreCase(title.replace(" ", ""), subCmd.replace(" ", ""))) {
						pPlayer.setCurrentTitleId(i);
						Output.positiveMessage(player, "You have set your title to \""+title+"\"");
						return;
					}
				}
				Output.simpleError(player, "You have no such title.");
			}
		}else{
			Output.displayTitles(player);
		}
	}

	private void playerResetSpawn(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.simpleError(sender, "Only players may perform this command.");
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer((Player) sender);
		if (pPlayer.isMurderer())
			Output.positiveMessage(sender,
					"You have reset your spawn to Chaos.");
		else
			Output.positiveMessage(sender,
					"You have reset your spawn to Order.");
		((Player) sender).setBedSpawnLocation(null);
		return;
	}

	private void playerSetPrivate(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer((Player) sender);
		if (pPlayer.isPrivate())
			Output.positiveMessage(sender, "You where already set private.");
		else {
			pPlayer.setPrivate(true);
			Output.positiveMessage(sender, "You have been set to private.");
		}
	}

	private void playerSetPublic(CommandSender sender) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final PseudoPlayer pPlayer = this.pm.getPlayer((Player) sender);
		if (!pPlayer.isPrivate())
			Output.positiveMessage(sender, "You where already set public.");
		else {
			pPlayer.setPrivate(false);
			Output.positiveMessage(sender, "You have been set to public.");
		}
	}

	private void playerSpawn(CommandSender sender) {
		if (sender instanceof Player) {
			final Player player = (Player) sender;
			final PseudoPlayer pPlayer = this.pm.getPlayer(player);
			if (pPlayer.getTimer().spawnTicks <= 0) {
				if(pPlayer.getTimer().goToSpawnTicks < 1) {
				pPlayer.getTimer().goToSpawnTicks = 100;
				Output.positiveMessage(player,
						"Returning to spawn in 10 seconds.");
				}else{
					Output.simpleError(player, "You are already spawning.");
				}
			} else {
				final int ticks = pPlayer.getTimer().spawnTicks;
				final int seconds = ticks / 10;
				Output.simpleError(player, "can't go to spawn, " + seconds / 60
						+ " minutes, " + seconds % 60 + " seconds remaining.");
			}
		} else
			Output.mustBePlayer(sender);
	}

	private void unignore(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Output.mustBePlayer(sender);
			return;
		}
		final Player player = (Player) sender;
		final PseudoPlayer pPlayer = this.pm.getPlayer(player);
		if (args.length < 1) {
			Output.simpleError(player, "Use \"/unignore (player) \"");
			if (pPlayer.getIgnored().isEmpty())
				Output.simpleError(player,
						"You are currently not ignoring any players.");
			else
				player.sendMessage(ChatColor.YELLOW
						+ Utils.listToString(Utils
								.UUIDArrayToUsernameArray(pPlayer.getIgnored())));
			return;
		}
		@SuppressWarnings("deprecation")
		final OfflinePlayer tPlayer = Bukkit.getOfflinePlayer(args[0]);
		if (!pPlayer.getIgnored().contains(tPlayer.getUniqueId())) {
			Output.simpleError(player, "You are currently not ignoring "
					+ tPlayer.getName() + ".");
			return;
		}
		Output.positiveMessage(player,
				"You are no longer ignoreing " + tPlayer.getName() + ".");
		pPlayer.getIgnored().add(tPlayer.getUniqueId());
	}
}
