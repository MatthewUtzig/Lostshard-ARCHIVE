package com.lostshard.Lostshard.Objects.InventoryGUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.Lostshard.Manager.PlayerManager;
import com.lostshard.Lostshard.Objects.PseudoPlayer;

public abstract class GUI {

	PlayerManager pm = PlayerManager.getManager();

	private PseudoPlayer player;
	private GUIItem[] items;
	private String name;
	
	public GUI(String name, PseudoPlayer pPlayer, GUIItem...items) {
		this.name = name;
		this.player = pPlayer;
		this.items = items;
	}
	
	public void setItem(int slot, GUIItem item) {
		items[slot] = item;
	}

	public void forceClose() {
		this.player.setGui(null);
		this.player.getOnlinePlayer().closeInventory();
	}

	public Inventory getGUI() {
		Inventory inv = Bukkit.createInventory(null,
				(int) Math.max(9, Math.ceil(items.length / 9) * 9), getName());
		ItemStack[] itemStacks = new ItemStack[items.length];
		Bukkit.broadcastMessage(""+items.length);
		for(int i=0; i<items.length; i++)
			itemStacks[i] = items[i].getItemStack();
		inv.setContents(itemStacks);
		Bukkit.broadcastMessage("Got gui with: "+items.length+" items.");
		return inv;
	}

	public PseudoPlayer getPlayer() {
		return this.player;
	}

	public void inventoryClick(InventoryClickEvent event) {
		if (event.getClickedInventory() == null)
			return;
		if (event.getClick() == null)
			return;
		if(!getPlayer().getGui().equals(this))
			return;
		event.setCancelled(true);
		GUIClick click = items[event.getSlot()].getClick();
		if(click != null && event.getCurrentItem() != null && event.getClick() != null && event.getWhoClicked() != null)
			click.click((Player) event.getWhoClicked(), getPlayer(), event.getCurrentItem(), event.getClick(), event.getInventory(), event.getSlot());
	}

	public void inventoryClose(InventoryCloseEvent event) {
		this.player.setGui(null);
	}

	public void inventoryInteract(InventoryInteractEvent event) {
		event.setCancelled(true);
	}

	public void openInventory(Player player) {
		this.pm.getPlayer(player).setGui(this);
		player.openInventory(this.getGUI());
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public GUIItem[] getItems() {
		return items;
	}

	public void setItems(GUIItem[] items) {
		this.items = items;
	}
}
