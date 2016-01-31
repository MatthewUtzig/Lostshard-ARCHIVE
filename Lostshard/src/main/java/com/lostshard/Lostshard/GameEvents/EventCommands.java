package com.lostshard.Lostshard.GameEvents;

import java.util.Date;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.lostshard.Lostshard.Commands.LostshardCommand;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.Output;

public class EventCommands extends LostshardCommand {

	private static EventManager em = EventManager.getManager();

	public EventCommands(Lostshard plugin, String[] commands) {
		super(plugin, "event");
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("event")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}

			final Player player = (Player) sender;

			if (args.length < 1) {
				Output.simpleError(player, "/event (create|start|stop|pause)");
				return true;
			}

			final String subcmd = args[0];
			if (subcmd.equalsIgnoreCase("create")) {
				try {
					final String name = args[1];
					final String title = args[2];
					final int size = Integer.parseInt(args[3]);
					final int duration = Integer.parseInt(args[4]);

					final long start = new Date().getTime();
					final long stop = start + duration * 1000;

					em.createEvent(this.getPlugin(), name, player, size, title, start, stop, args);
				} catch (final Exception e) {
					Output.simpleError(player, "/event create (event) (title) (size) (duration)");
				}
				return true;
			}

			if (subcmd.equalsIgnoreCase("start")) {

				return true;
			}

			if (subcmd.equalsIgnoreCase("stop")) {

				return true;
			}

			if (subcmd.equalsIgnoreCase("pause")) {

				return true;
			}
		}
		return false;
	}
}
