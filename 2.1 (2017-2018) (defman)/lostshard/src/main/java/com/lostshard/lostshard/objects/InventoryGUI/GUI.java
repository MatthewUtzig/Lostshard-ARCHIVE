package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.Player.PseudoPlayer;

public abstract class GUI {
	PlayerManager pm = PlayerManager.getManager();
	private PseudoPlayer player;
	private List<GUIItem> items;
	private String name;

	public GUI(String name, PseudoPlayer pPlayer, GUIItem... items) {
		this.name = name;
		this.player = pPlayer;
		this.items = new ArrayList<GUIItem>(Arrays.asList(items));
	}

	public void forceClose() {
		this.player.setGui(null);
		this.player.getOnlinePlayer().closeInventory();
	}

	public Inventory getGUI() {
		final Inventory inv = Bukkit.createInventory(null,
				(int) Math.max(9.0D, Math.ceil(this.items.size() / 9.0D) * 9.0D), this.getName());
		if (this.items == null || this.items.isEmpty()) {
			return inv;
		}
		final ItemStack[] itemStacks = new ItemStack[this.items.size()];
		for (int i = 0; i < this.items.size(); i++) {
			itemStacks[i] = this.items.get(i).getItemStack();
		}
		inv.setContents(itemStacks);
		return inv;
	}

	public List<GUIItem> getItems() {
		return this.items;
	}

	public String getName() {
		return this.name;
	}

	public PseudoPlayer getPlayer() {
		return this.player;
	}

	public void inventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null) {
			return;
		}
		if (event.getClick() == null) {
			return;
		}
		if (!this.getPlayer().getGui().equals(this)) {
			return;
		}
		event.setCancelled(true);
		if (event.getSlot() >= this.items.size()) {
			return;
		}
		final GUIClick click = this.items.get(event.getSlot()).getClick();
		if (click != null && event.getCurrentItem() != null && event.getClick() != null
				&& event.getWhoClicked() != null) {
			click.click((Player) event.getWhoClicked(), this.getPlayer(), event.getCurrentItem(), event.getClick(),
					event.getInventory(), event.getSlot());
		}
	}

	public void inventoryClose(InventoryCloseEvent event) {
		this.player.setGui(null);
	}

	public void inventoryInteract(InventoryInteractEvent event) {
		event.setCancelled(true);
	}

	public void openInventory(Player player) {
		player.openInventory(this.getGUI());
		player.updateInventory();
		this.getPlayer().setGui(this);
	}

	public void setItem(int slot, GUIItem item) {
		this.items.set(slot, item);
	}

	public void setItems(GUIItem... items) {
		this.items = new ArrayList<GUIItem>(Arrays.asList(items));
	}

	public void setItems(List<GUIItem> items) {
		this.items = items;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}
}
