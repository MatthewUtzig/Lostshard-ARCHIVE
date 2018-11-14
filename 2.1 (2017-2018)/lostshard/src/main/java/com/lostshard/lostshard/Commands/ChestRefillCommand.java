package com.lostshard.lostshard.Commands;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Intake.Sender;
import com.lostshard.lostshard.Manager.ChestRefillManager;
import com.lostshard.lostshard.Utils.Output;
import com.lostshard.lostshard.Utils.SpellUtils;
import com.sk89q.intake.Command;
import com.sk89q.intake.Require;
import com.sk89q.intake.parametric.annotation.Optional;
import com.sk89q.intake.parametric.annotation.Range;

public class ChestRefillCommand  {

	ChestRefillManager cm = ChestRefillManager.getManager();

	@Command(aliases = { "create" }, desc = "Creates a dungeon chest", usage = "<minMinuters> <maxMinuters>")
	@Require("lostshard.admin.dc.create")
	public void create(@Sender Player player, @Range(min=1) int min, @Range(min=1) int max) {
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
		
		if(min > max) {
			Output.simpleError(player, "Max time cannot be less than min time.");
			return;
		}
		min *= 60000;
		max *= 60000;

		final ItemStack[] items = ((Chest) block.getState()).getInventory().getContents();
		cr = new ChestRefill(block.getLocation(), min, max, items, 0);
		this.cm.add(cr);
	}

	@Command(aliases = { "fill" }, desc = "Filles a dungeon chest")
	@Require("lostshard.admin.dc.fill")
	public void dcFill(@Sender Player player) {
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

	@Command(aliases = { "remove" }, desc = "Deletes a dungeon chest")
	@Require("lostshard.admin.dc.remove")
	public void dcRemove(@Sender Player player) {
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

	@Command(aliases = { "setcontents" }, desc = "Set contents of a dungeon chest")
	@Require("lostshard.admin.dc.setcontents")
	public void dcSetContents(@Sender Player player) {
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
	
	@Command(aliases = { "", "help" }, desc = "Dungeon chest help", usage = "<page>")
	@Require("lostshard.admin.dc")
	public void dc(@Sender Player player, @Optional(value ="1") @Range(min=0) int page) {
		Output.positiveMessage(player, "Dungeon Chest");
		Output.positiveMessage(player, "/dc create (minMinuters) (maxMinuters)");
		Output.positiveMessage(player, "/dc remove");
		Output.positiveMessage(player, "/dc setcontents");
		Output.positiveMessage(player, "/dc fill");
	}
}
