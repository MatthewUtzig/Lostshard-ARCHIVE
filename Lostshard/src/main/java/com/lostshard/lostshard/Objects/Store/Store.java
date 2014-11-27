package com.lostshard.lostshard.Objects.Store;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class Store {

	private int id;
	private int npcId;
	private int plotId;
	private Map<ItemStack, Integer> itemsForSale = new HashMap<ItemStack, Integer>();
	private Map<ItemStack, Integer> itemsForBuy = new HashMap<ItemStack, Integer>();
	
}
