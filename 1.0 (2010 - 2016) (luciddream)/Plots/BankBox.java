package com.lostshard.RPG.Plots;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_7_R1.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

import com.lostshard.RPG.Utils.IDAPI;

import net.minecraft.server.v1_7_R1.EntityPlayer;
import net.minecraft.server.v1_7_R1.InventoryLargeChest;
import net.minecraft.server.v1_7_R1.NBTBase;
import net.minecraft.server.v1_7_R1.NBTTagCompound;
import net.minecraft.server.v1_7_R1.NBTTagList;

//import net.minecraft.server.EntityPlayer;
//import net.minecraft.server.InventoryLargeChest;

public class BankBox {
	private boolean _largerbank;
	private TileEntityVirtualChest chest;
	private TileEntityVirtualChest chest2;
	private InventoryLargeChest lc;
	
	public BankBox(String serializedString, boolean largerbank) {
		_largerbank = largerbank;
		
		chest = new TileEntityVirtualChest();
		chest2 = new TileEntityVirtualChest();
		
		lc = new InventoryLargeChest("Large chest", chest, chest2);
		
		// break up by commas
		if(serializedString.trim().equalsIgnoreCase("")) {
			// empty bank
		}
		else {
			String[] splitSerializedString = serializedString.split(",");
			for(String itemData : splitSerializedString) {
				try {
					String[] splitData = itemData.split("@");
					int slot = Integer.parseInt(splitData[0]);
					int id = Integer.parseInt(splitData[1]);
					int count = Integer.parseInt(splitData[2]);
					int damage = Integer.parseInt(splitData[3]);
					addItem(slot,id,count,damage);
					if(splitData.length == 4) {
						addItem(slot,id,count,damage);
					}
					else {
						ArrayList<Enchantment> enchantments = new ArrayList<Enchantment>();
						ArrayList<Integer> levels = new ArrayList<Integer>();
						int numEnchantments = (splitData.length - 4)/2;
						for(int i=0; i<numEnchantments; i++) {
							int enchantmentId = Integer.parseInt(splitData[4+i*2]);
							int enchantmentLevel = Integer.parseInt(splitData[4+i*2+1]);
							Enchantment enchantment = Enchantment.getById(enchantmentId);
							enchantments.add(enchantment);
							levels.add(enchantmentLevel);
						}
						addItem(slot,id,count,damage,enchantments,levels);
					}
				}
				catch(Exception e) {
					System.out.println("Failed to read item: " + itemData+" >>"+e.toString());
				}
			}
		}
	}
	
	public void open(Player player) {
		EntityPlayer eh = ((CraftPlayer)player).getHandle();
		// Chest time!
		if(_largerbank) {
			if(lc == null)
				System.out.println("LCNULL");
			eh.openContainer(lc);
		}
		else {
			if(lc == null)
				System.out.println("CNULL");
			eh.openContainer(chest);
		}
	}
	
	private static net.minecraft.server.v1_7_R1.Enchantment getNMSEnchantmentById(int enchantmentId) {
		if(enchantmentId == 0)
			return net.minecraft.server.v1_7_R1.Enchantment.PROTECTION_ENVIRONMENTAL;
		else if(enchantmentId == 1)
			return net.minecraft.server.v1_7_R1.Enchantment.PROTECTION_FIRE;
		else if(enchantmentId == 2)
			return net.minecraft.server.v1_7_R1.Enchantment.PROTECTION_FALL;
		else if(enchantmentId == 3)
			return net.minecraft.server.v1_7_R1.Enchantment.PROTECTION_EXPLOSIONS;
		else if(enchantmentId == 4)
			return net.minecraft.server.v1_7_R1.Enchantment.PROTECTION_PROJECTILE;
		else if(enchantmentId == 5)
			return net.minecraft.server.v1_7_R1.Enchantment.OXYGEN;
		else if(enchantmentId == 6)
			return net.minecraft.server.v1_7_R1.Enchantment.WATER_WORKER;
		else if(enchantmentId == 16)
			return net.minecraft.server.v1_7_R1.Enchantment.DAMAGE_ALL;
		else if(enchantmentId == 17)
			return net.minecraft.server.v1_7_R1.Enchantment.DAMAGE_UNDEAD;
		else if(enchantmentId == 18)
			return net.minecraft.server.v1_7_R1.Enchantment.DAMAGE_ARTHROPODS;
		else if(enchantmentId == 19)
			return net.minecraft.server.v1_7_R1.Enchantment.KNOCKBACK;
		else if(enchantmentId == 20)
			return net.minecraft.server.v1_7_R1.Enchantment.FIRE_ASPECT;
		else if(enchantmentId == 21)
			return net.minecraft.server.v1_7_R1.Enchantment.LOOT_BONUS_MOBS;
		else if(enchantmentId == 32)
			return net.minecraft.server.v1_7_R1.Enchantment.DIG_SPEED;
		else if(enchantmentId == 33)
			return net.minecraft.server.v1_7_R1.Enchantment.SILK_TOUCH;
		else if(enchantmentId == 34)
			return net.minecraft.server.v1_7_R1.Enchantment.DURABILITY;
		else if(enchantmentId == 35)
			return net.minecraft.server.v1_7_R1.Enchantment.LOOT_BONUS_BLOCKS;
		else if(enchantmentId == 48)
			return net.minecraft.server.v1_7_R1.Enchantment.ARROW_DAMAGE;
		else if(enchantmentId == 49)
			return net.minecraft.server.v1_7_R1.Enchantment.ARROW_KNOCKBACK;
		else if(enchantmentId == 50)
			return net.minecraft.server.v1_7_R1.Enchantment.ARROW_FIRE;
		else if(enchantmentId == 51)
			return net.minecraft.server.v1_7_R1.Enchantment.ARROW_INFINITE;
		return null;
	}
	
	public void addItem(int slot, int id, int count, int damage) {	
		net.minecraft.server.v1_7_R1.ItemStack obItemStack = new net.minecraft.server.v1_7_R1.ItemStack(net.minecraft.server.v1_7_R1.Item.d(id), count, damage);
		if(_largerbank)
			lc.setItem(slot, obItemStack);
		else
			chest.setItem(slot, obItemStack);
	}
	
	public void addItem(int slot, int id, int count, int damage, ArrayList<Enchantment> enchantments, ArrayList<Integer> levels) {
		net.minecraft.server.v1_7_R1.ItemStack obItemStack = new net.minecraft.server.v1_7_R1.ItemStack(net.minecraft.server.v1_7_R1.Item.d(id), count, damage);
		for(int i=0; i<enchantments.size(); i++) {
			net.minecraft.server.v1_7_R1.Enchantment nmsEnchantment = getNMSEnchantmentById(enchantments.get(i).getId());
			if(nmsEnchantment != null) {
				obItemStack.addEnchantment(nmsEnchantment, levels.get(i));
			}
		}
		if(_largerbank)
			lc.setItem(slot, obItemStack);
		else
			chest.setItem(slot, obItemStack);
	}
	
	public String getSerialized() {
		ArrayList<net.minecraft.server.v1_7_R1.ItemStack> obItemStacks = new ArrayList<net.minecraft.server.v1_7_R1.ItemStack>();
		ArrayList<Integer> slots = new ArrayList<Integer>();
		// loop through the chest, pull out all the items that aren't null
		if(_largerbank) {
			for(int i=0; i<54; i++) {
				net.minecraft.server.v1_7_R1.ItemStack obItemStack = lc.getItem(i);
				if(obItemStack != null) {
					obItemStacks.add(obItemStack);
					slots.add(i);
				}
			}
		}
		else {
			for(int i=0; i<27; i++) {
				net.minecraft.server.v1_7_R1.ItemStack obItemStack = chest.getItem(i);
				if(obItemStack != null) {
					obItemStacks.add(obItemStack);
					slots.add(i);
				}
			}
		}
		
		String serializedString = "";
		int numObItemStacks = obItemStacks.size();
		for(int i=0; i<numObItemStacks; i++) {
			net.minecraft.server.v1_7_R1.ItemStack obItemStack = obItemStacks.get(i);
			
			if(obItemStack == null || obItemStack.getItem() == null || obItemStack.getItem().getName() == null)
				continue;
			
			serializedString += slots.get(i) + "@"; // slot
			serializedString += net.minecraft.server.v1_7_R1.Item.b(obItemStack.getItem()) + "@"; // id PROBABLY FUCKED
			System.out.println(net.minecraft.server.v1_7_R1.Item.b(obItemStack.getItem()));
			serializedString += obItemStack.count + "@"; // count
			serializedString += obItemStack.getData(); // damage
			if(obItemStack.hasEnchantments()) {
				NBTTagList enchantments = obItemStack.getEnchantments();
				if(enchantments.size() > 0) {
					for(int f=0; f<enchantments.size(); f++) {
						NBTTagCompound nbtCompound = (NBTTagCompound) enchantments.get(f);
						int enchantmentId = nbtCompound.getShort("id");
						int level = nbtCompound.getShort("lvl");
						serializedString += "@"+enchantmentId;
						serializedString += "@"+level;
					}
				}
			}
			if(i < numObItemStacks-1)
				serializedString+=",";
		}
		
		return serializedString;
	}
	
}
