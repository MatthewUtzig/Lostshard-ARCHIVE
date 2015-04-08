package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Objects.PseudoPlayer;

public abstract class InventoryGUI {

	private Inventory GUI;
	private GUIType type;
	private PseudoPlayer player;
	
	public InventoryGUI(int size, String name, GUIType type, PseudoPlayer pPlayer) {
		super();
		this.GUI = Bukkit.createInventory(null, size, name);
		this.type = type;
		this.player = pPlayer;
		player.setGui(this);
		optionSelector();
	}
	
	abstract public void optionSelector();
	abstract public void onClick(InventoryClickEvent event);
	abstract public void onClose(InventoryCloseEvent event);
	
	public void inventoryClick(InventoryClickEvent event) {
		event.setCancelled(true);
		onClick(event);
	}
	
	public void inventoryClose(InventoryCloseEvent event) {
		onClose(event);
	}
	
	public void openInventory(Player player) {
		player.openInventory(GUI);
	}
	
	public void addOption(ItemStack item) {
		GUI.addItem(item);
	}
	
	public void close() {
		for(HumanEntity he : GUI.getViewers())
			he.closeInventory();
		player.setGui(null);
		GUI.clear();
	}

	public Inventory getGUI() {
		return GUI;
	}

	public void setGUI(Inventory gUI) {
		GUI = gUI;
	}

	public GUIType getType() {
		return type;
	}

	public void setType(GUIType type) {
		this.type = type;
	}

	public PseudoPlayer getPlayer() {
		return player;
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}
}
