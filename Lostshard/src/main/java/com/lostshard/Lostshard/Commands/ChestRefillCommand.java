package com.lostshard.Lostshard.Commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Manager.ChestRefillManager;
import com.lostshard.Lostshard.Objects.ChestRefill;
import com.lostshard.Lostshard.Utils.Output;
import com.lostshard.Lostshard.Utils.SpellUtils;

public class ChestRefillCommand extends LostshardCommand {

	ChestRefillManager cm = ChestRefillManager.getManager();

	public ChestRefillCommand(Lostshard plugin) {
		super(plugin, "dc");
	}

	private void dcCreate(Player player, String[] args) {
		if (args.length < 3) {
			Output.simpleError(player, "/dc create (minMinuters) (maxMinuters)");
			return;
		}
		final Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if (!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		ChestRefill cr = this.cm.getChest((Chest) block.getState());
		if (cr != null) {
			Output.simpleError(player, "Thats already a Dungeon Chest.");
			return;
		}
		long rangeMin;
		long rangeMax;
		try {
			rangeMin = Integer.parseInt(args[1]);
			rangeMax = Integer.parseInt(args[2]);
		} catch (final Exception e) {
			Output.simpleError(player, "/dc create (minMinuters) (maxMinuters)");
			return;
		}

		rangeMin *= 60000;
		rangeMax *= 60000;

		final ItemStack[] items = ((Chest) block.getState()).getInventory().getContents();
		cr = new ChestRefill(block.getLocation(), rangeMin, rangeMax, items, 0);
		this.cm.add(cr);
	}

	private void dcFill(Player player) {
		final Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if (!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		final ChestRefill cr = this.cm.getChest((Chest) block.getState());
		if (cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		cr.refill();
		Output.positiveMessage(player, "You have refilled the Dungeon Chest.");
	}

	private void dcRemove(Player player) {
		final Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if (!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		final ChestRefill cr = this.cm.getChest((Chest) block.getState());
		if (cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		this.cm.remove(cr);
		Output.positiveMessage(player, "You have removed the Dungeon Chest.");
	}

	private void dcSetContents(Player player) {
		final Block block = player.getTargetBlock(SpellUtils.invisibleBlocks, 5);
		if (!block.getState().getType().equals(Material.CHEST)) {
			Output.simpleError(player, "Invalid target.");
			return;
		}
		final ChestRefill cr = this.cm.getChest((Chest) block.getState());
		if (cr == null) {
			Output.simpleError(player, "Thats not a Dungeon Chest.");
			return;
		}
		cr.setItems(((Chest) block.getState()).getInventory().getContents());
		cr.save();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
		if (cmd.getName().equalsIgnoreCase("dc")) {
			if (!(sender instanceof Player)) {
				Output.mustBePlayer(sender);
				return true;
			}
			final Player player = (Player) sender;
			if (args.length < 1) {
				Output.positiveMessage(player, "Dungeon Chest");
				Output.positiveMessage(player, "/dc create (minMinuters) (maxMinuters)");
				Output.positiveMessage(player, "/dc remove");
				Output.positiveMessage(player, "/dc setcontents");
				Output.positiveMessage(player, "/dc fill");
				return true;
			}
			final String subCmd = args[0];
			if (subCmd.equalsIgnoreCase("create"))
				this.dcCreate(player, args);
			else if (subCmd.equalsIgnoreCase("remove"))
				this.dcRemove(player);
			else if (subCmd.equalsIgnoreCase("setContents"))
				this.dcSetContents(player);
			else if (subCmd.equalsIgnoreCase("fill"))
				this.dcFill(player);
			else {
				Output.positiveMessage(player, "Dungeon Chest");
				Output.positiveMessage(player, "/dc create (minMinuters) (maxMinuters)");
				Output.positiveMessage(player, "/dc remove");
				Output.positiveMessage(player, "/dc setcontents");
				Output.positiveMessage(player, "/dc fill");
				return true;
			}
			return true;
		}
		return false;
	}
}
