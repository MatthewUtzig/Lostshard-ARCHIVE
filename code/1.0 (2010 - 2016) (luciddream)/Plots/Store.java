package com.lostshard.RPG.Plots;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lostshard.RPG.CustomContent.CustomArmor;
import com.lostshard.RPG.CustomContent.CustomTools;
import com.lostshard.RPG.CustomContent.CustomArmor.ArmorType;
import com.lostshard.RPG.CustomContent.CustomTools.ToolType;
import com.lostshard.RPG.Utils.Output;
import com.lostshard.RPG.Utils.Utils;

public class Store {
	private int _id;
	private String _name;
	private int _size;
	private Location _location;
	private int _type = 0;
	private ArrayList<ItemStack> _itemsForSale = new ArrayList<ItemStack>();
	private ArrayList<Integer> _itemStock = new ArrayList<Integer>();
	private ArrayList<Integer> _prices = new ArrayList<Integer>();
	private ArrayList<Integer> _restockRates = new ArrayList<Integer>();
	
	public Store(String name, int size, Location location) {
		_name = name;
		_size = size;
		_location = location;
	}
	
	public Store(int id, String name, int size, Location location, ArrayList<ItemStack> itemsForSale, ArrayList<Integer> itemStock, ArrayList<Integer> prices, ArrayList<Integer> restockRates) {
		_id = id;
		_name = name;
		_size = size;
		_location = location;
		_itemsForSale = itemsForSale;
		_itemStock = itemStock;
		_prices = prices;
		_restockRates = restockRates;
	}
	
	public void setId(int id) {
		_id = id;
	}
	
	public int getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getSize() {
		return _size;
	}
	
	public Location getLocation() {
		return _location;
	}
	
	public void setLocation(Location location) {
		_location = location;
	}
	
	public int getType() {
		return _type;
	}
	
	public void setType(int type) {
		_type = type;
	}
	
	public String getSerializedStoreData() {
		String serialized = "";
		int numItems = _itemsForSale.size();
		for(int i=0; i<numItems; i++) {
			String thisItem = "";
			if(_type == 1) {
				thisItem = _itemsForSale.get(i).getTypeId()+"#"+_itemStock.get(i)+"#"+_prices.get(i)+"#"+_itemsForSale.get(i).getDurability();
			}
			else if(_type == 0)
				thisItem = _itemsForSale.get(i).getTypeId()+"-"+_itemStock.get(i)+"-"+_prices.get(i)+"-"+_restockRates.get(i);
			
			if(i < numItems-1)
				thisItem+=",";
			serialized+= thisItem;
		}
		return serialized;
	}
	
	public void addItem(int itemId, int stock, short durability, int price, int restockRate) {
		ItemStack itemStack = new ItemStack(itemId, 1);
		itemStack.setDurability(durability);
		_itemsForSale.add(itemStack);
		_itemStock.add(stock);
		_prices.add(price);
		_restockRates.add(restockRate);
	}
	
	/*public void removeItem(int itemId) {
		for(ItemStack itemStack : _itemsForSale) {
			if(itemStack.getTypeId() == itemId) {
				_itemsForSale.remove(itemStack);
				break;
			}
		}
	}
	
	public void removeItem(int itemId, int durability) {
		for(ItemStack itemStack : _itemsForSale) {
			if(itemStack.getTypeId() == itemId && itemStack.getDurability() == (short)durability) {
				_itemsForSale.remove(itemStack);
			}
		}
	}*/
	
	public void removeItem(int index) {
		_itemsForSale.remove(index);
		_itemStock.remove(index);
		_prices.remove(index);
		_restockRates.remove(index);
	}
	
	public ArrayList<ItemStack> getItemsForSale() {
		return _itemsForSale;
	}
	
	public ArrayList<Integer> getItemStocks() {
		return _itemStock;
	}
	
	public ArrayList<Integer> getItemPrices() {
		return _prices;
	}
	
	public ArrayList<Integer> getRestockRates() {
		return _restockRates;
	}
	
	public int getNumItems() {
		return _itemsForSale.size();
	}
	
	public ItemStack getItem(int index) {
		return _itemsForSale.get(index);
	}
	
	public int getQuantity(int index) {
		return _itemStock.get(index);
	}
	
	public int getPrice(int index) {
		return _prices.get(index);
	}
	
	public int getRestockRate(int index) {
		return _restockRates.get(index);
	}
	
	public void setStock(int index, int quantity) {
		_itemStock.set(index, quantity);
	}
	
	public void restock() {
		if(_type != 1) {
			int numItems = _itemsForSale.size();
			for(int i=0; i<numItems; i++) {
				int maxStock = _restockRates.get(i) * 10;
				int stocking = _itemStock.get(i)+_restockRates.get(i);
				if(stocking > maxStock)
					stocking = maxStock;
				_itemStock.set(i, stocking);
			}
		}
	}
	
	public void outputList(Player player) {
		Output.positiveMessage(player, "-"+getName()+"-");
		Output.positiveMessage(player, "Use \"/vendor buy (item number) (quantity)\"");
		Output.positiveMessage(player, "Ex: \"/vendor buy 1 24\"");
		if(_itemsForSale.size() <= 0) {
			Output.simpleError(player, "No items for sale.");
		}
		else {
			int numItems = _itemsForSale.size();
			for(int i=0; i<numItems; i++) {
				ItemStack itemForSale = _itemsForSale.get(i);
				
				if(itemForSale.getType().equals(Material.INK_SACK) || itemForSale.getType().equals(Material.WOOL) || itemForSale.getTypeId() == 44) {
					String displayString = (i+1)+". "+Utils.itemToName(itemForSale.getTypeId(), itemForSale.getDurability())+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
					player.sendMessage(ChatColor.YELLOW+displayString);
					continue;
				}
				
				ToolType toolType = CustomTools.ToolType.getById(itemForSale.getTypeId());
				ArmorType armorType = CustomArmor.ArmorType.getById(itemForSale.getTypeId());
				
				if(toolType != null) {
					// must be a tool
					if(itemForSale.getType().equals(Material.WOOD_SWORD) || itemForSale.getType().equals(Material.STONE_SWORD) || itemForSale.getType().equals(Material.IRON_SWORD) || itemForSale.getType().equals(Material.DIAMOND_SWORD) || itemForSale.getType().equals(Material.GOLD_SWORD)) {
						// its a sword
						if(itemForSale.getDurability() <= (short)0 && itemForSale.getDurability() > (short)-5) {
							String displayString = (i+1)+". Pristine "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
						else if(itemForSale.getDurability() == (short)-5) {
							String displayString = (i+1)+". Sharpened "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
						else {
							String displayString = (i+1)+". "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
					}
					else {
						// Its a tool
						if(itemForSale.getDurability() <= (short)0 && itemForSale.getDurability() > (short)0-toolType.getDurability()*.4) {
							String displayString = (i+1)+". Pristine "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
						else if(itemForSale.getDurability() <= (short)0-toolType.getDurability()*.4) {
							String displayString = (i+1)+". Reinforced "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
						else {
							String displayString = (i+1)+". "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
							player.sendMessage(ChatColor.YELLOW+displayString);
						}
					}
				}
				else if(armorType != null) {
					// must be armor
					if(itemForSale.getDurability() <= (short)0 && itemForSale.getDurability() > (short)-10) {
						String displayString = (i+1)+". Pristine "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
						player.sendMessage(ChatColor.YELLOW+displayString);
					}
					else if(itemForSale.getDurability() == (short)-10) {
						String displayString = (i+1)+". Hardened "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
						player.sendMessage(ChatColor.YELLOW+displayString);
					}
					else {
						String displayString = (i+1)+". "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
						player.sendMessage(ChatColor.YELLOW+displayString);
					}
				}
				else {
					// Not a tool or armor
					String displayString = (i+1)+". "+_itemsForSale.get(i).getType().name()+" (Price: "+_prices.get(i)+") (In Stock: "+_itemStock.get(i)+")";
					player.sendMessage(ChatColor.YELLOW+displayString);
				}
			}
		}
	}
	
}
