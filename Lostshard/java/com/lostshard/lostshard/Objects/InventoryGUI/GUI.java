package com.lostshard.lostshard.Objects.InventoryGUI;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.lostshard.lostshard.Manager.PlayerManager;
import com.lostshard.lostshard.Objects.PseudoPlayer;

public abstract class GUI {

	PlayerManager pm = PlayerManager.getManager();
	
	private Inventory GUI = null;
	private PseudoPlayer player;
	
	public GUI(int size, String name, PseudoPlayer pPlayer) {
		super();
		this.GUI = Bukkit.createInventory(null, (int)Math.max(9,Math.ceil(size/9)*9), name);
		this.player = pPlayer;
		player.setGui(this);
	}
	
	public GUI(PseudoPlayer pPlayer) {
		super();
		this.player = pPlayer;
	}
	
	public void addOption(ItemStack item) {
		GUI.addItem(item);
	}
	public void addOption(String name, Material mat) {
		ItemStack item = new ItemStack(mat);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		item.setItemMeta(itemMeta);
		GUI.addItem(item);
	}
	
	public void addOption(String name, Material mat, List<String> lore) {
		ItemStack item = new ItemStack(mat);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(name);
		itemMeta.setLore(lore);
		item.setItemMeta(itemMeta);
		GUI.addItem(item);
	}
	
	public void forceClose() {
		player.setGui(null);
		player.getOnlinePlayer().closeInventory();
	}
	
	public Inventory getGUI() {
		return GUI;
	}
	
	public PseudoPlayer getPlayer() {
		return player;
	}
	
	public void inventoryClick(InventoryClickEvent event) {
		if(event.getClickedInventory() == null)
			return;
		if(!event.getClickedInventory().equals(GUI))
			return;
		if(event.getCurrentItem() == null)
			return;
		if(event.getAction() == null)
			return;
		if(event.getCurrentItem().getItemMeta() == null)
			return;
		event.setCancelled(true);
		onClick(event);
	}
	
	public void inventoryClose(InventoryCloseEvent event) {
		player.setGui(null);
	}
	
	public void inventoryInteract(InventoryInteractEvent event) {
		if(!event.getInventory().equals(GUI))
			return;
		event.setCancelled(true);
	}
	
	abstract public void onClick(InventoryClickEvent event);

	public void openInventory(Player player) {
		pm.getPlayer(player).setGui(this);
		player.openInventory(GUI);
	}

	abstract public void optionSelector();

	public void setGUI(Inventory GUI) {
		this.GUI = GUI;
	}

	public void setPlayer(PseudoPlayer player) {
		this.player = player;
	}
}
