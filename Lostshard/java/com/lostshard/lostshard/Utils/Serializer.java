package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.parser.JSONParser;

import com.google.gson.Gson;
import com.lostshard.lostshard.Main.Lostshard;

public class Serializer {

	public static JSONParser parser = new JSONParser();
	public static Gson gson = new Gson();
	
	@SuppressWarnings("unchecked")
	public static List<String> deserializeStringArray(String stringArray) {
		return gson.fromJson(stringArray, List.class);
	}
	
	public static String serializeStringArray(List<String> stringArray) {
		return gson.toJson(stringArray);
	}
	
	public static int[] deserializeIntegerArray(String integerArray) {
		return gson.fromJson(integerArray, int[].class);
	}
	
	public static String serializeIntegerArray(int[] integerArray) {
		return gson.toJson(integerArray);
	}
	
	public static Location deserializeLocation(String locationString) {
		return gson.fromJson(locationString, Location.class);
	}

	public static String serializeLocation(Location location) {
		return gson.toJson(location.serialize());
	}

	public static List<UUID> deserializeUUIDList(String uuidString) {
		String[] array = gson.fromJson(uuidString, String[].class);
		List<UUID> uuids = new ArrayList<UUID>();
		for (Object s : array)
			uuids.add(UUID.fromString((String) s));
		return uuids;
	}

	public static String serializeUUIDList(List<UUID> uuids) {
		List<String> array = new ArrayList<String>();
		for (UUID uuid : uuids)
			array.add(uuid.toString());
		return gson.toJson(array);
	}
	
	public static String toJsonList(List<String> list) {
		return gson.toJson(list);
	}

	public static String serializeItems(ItemStack[] items) {
		List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < items.length; i++) {
			ItemStack is = items[i];
			if (is == null)
				is = new ItemStack(Material.AIR);
			array.add(is.serialize());
		}
		return gson.toJson(array);
	}
	
	
	@SuppressWarnings("unchecked")
	public static ItemStack fromJsonToItemStack(String s) {
		Map<String, Object> map = gson.fromJson(
				s, Map.class);
		ItemStack is;
		try {
			is = ItemStack.deserialize(map);
		} catch (Exception e) {
			is = new ItemStack(Material.AIR);
			e.printStackTrace();
		}
		return is;
	}

	public static String toJsonItemStack(ItemStack item) {
		return gson.toJson(item.serialize());
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack[] deserializeItems(String string) {
		ItemStack[] rs;
		try {
			List<Map<String, Object>> stacks = gson.fromJson(string, new ArrayList<Map<String, Object>>().getClass());
			rs = new ItemStack[stacks.size()];
			for (int i = 0; i < stacks.size(); i++) {
				Map<String, Object> map = stacks.get(i);
//				ConfigurationSerialization.registerClass(ItemMeta.class, "ItemMeta");
//				map.replace("meta", (ItemMeta) ConfigurationSerialization.deserializeObject((Map<String, Object>) map.get("meta"), ConfigurationSerialization.getClassByAlias("ItemMeta")));
				rs[i] = ItemStack.deserialize(map);
			}
			return rs;
		} catch (Exception e) {
			Lostshard.log.log(Level.WARNING, "[Inventory-Serialization] "
					+ string);
		}
		rs = new ItemStack[] { new ItemStack(Material.AIR) };
		return rs;
	}
}