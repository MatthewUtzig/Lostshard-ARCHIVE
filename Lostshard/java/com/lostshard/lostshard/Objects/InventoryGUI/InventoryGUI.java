package com.lostshard.lostshard.Objects.InventoryGUI;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public abstract class InventoryGUI {

	PlayerManager pm = PlayerManager.getManager();
	
	private Inventory GUI;
	private GUIType type;
	private PseudoPlayer player;
	
	public InventoryGUI(int size, String name, GUIType type, PseudoPlayer pPlayer) {
		super();
		this.GUI = Bukkit.createInventory(null, Math.max(9,size), name);
		this.type = type;
		this.player = pPlayer;
		player.setGui(this);
		optionSelector();
	}
	
	abstract public void optionSelector();
	abstract public void onClick(InventoryClickEvent event);
	abstract public void onClose(InventoryCloseEvent event);
	
	public void inventoryInteract(InventoryInteractEvent event) {
		if(!event.getInventory().equals(GUI))
			return;
		event.setCancelled(true);
	}
	
	public void inventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory() == null)
			return;
		if(!event.getClickedInventory().equals(GUI))
			return;
		if(event.getCurrentItem() == null)
			return;
		event.setCancelled(true);
		onClick(event);
	}
	
	public void inventoryClose(InventoryCloseEvent event) {
		onClose(event);
		player.setGui(null);
	}
	
	public void openInventory(Player player) {
		pm.getPlayer(player).setGui(this);
		player.openInventory(GUI);
	}
	
	public void addOption(ItemStack item) {
		GUI.addItem(item);
	}
	
	public void forceClose() {
		player.setGui(null);
		player.getOnlinePlayer().closeInventory();
	}

	public Inventory getGUI() {
		return GUI;
	}

	public void setGUI(Inventory GUI) {
		this.GUI = GUI;
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
