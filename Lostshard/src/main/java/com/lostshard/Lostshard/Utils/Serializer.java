package com.lostshard.Lostshard.Utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.lostshard.Lostshard.Main.Lostshard;

public class Serializer {

	public static int[] deserializeIntegerArray(String integerArray) {
		return gson.fromJson(integerArray, int[].class);
	}

	@SuppressWarnings("serial")
	public static ItemStack[] deserializeItems(String string) {
		ItemStack[] rs;
		try {
			Type itemlist = new TypeToken<List<Map<String, Object>>>(){}.getType();
			final List<Map<String, Object>> stacks = gson.fromJson(string, itemlist);
			rs = new ItemStack[stacks.size()];
			for (int i = 0; i < stacks.size(); i++) {
				try {
					final Map<String, Object> map = stacks.get(i);
					if (map.containsKey("damage"))
						map.replace("damage",
								((Number) map.get("damage")).shortValue());
	
					if (map.containsKey("amount"))
						map.replace("amount",
								((Number) map.get("amount")).intValue()); 
					rs[i] = ItemStack.deserialize(map);
				} catch(final Exception e) {
					Lostshard.log.log(Level.WARNING, "[Inventory-Serialization] Single item"
							+ string);
					e.printStackTrace();
					rs[i] = new ItemStack(Material.AIR);
				}
			}
			return rs;
		} catch (final Exception e) {
			Lostshard.log.log(Level.WARNING, "[Inventory-Serialization] "
					+ string);
			e.printStackTrace();
		}
		rs = new ItemStack[] { new ItemStack(Material.AIR) };
		return rs;
	}
	
	public static Location deserializeLocation(String locationString) {
		try {
			final Object jo = parser.parse(locationString);
			final JSONObject map = (JSONObject) jo;

			final World world = Bukkit.getWorld(map.get("world").toString());
			final Double x = ((Number) map.get("x")).doubleValue();
			final Double y = ((Number) map.get("y")).doubleValue();
			final Double z = ((Number) map.get("z")).doubleValue();
			final Float yaw = ((Number) map.get("yaw")).floatValue();
			final Float pitch = ((Number) map.get("pitch")).floatValue();

			return new Location(world, x, y, z, yaw, pitch);
		} catch (final ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static List<String> deserializeStringArray(String stringArray) {
		return gson.fromJson(stringArray, List.class);
	}

	public static List<UUID> deserializeUUIDList(String uuidString) {
		final String[] array = gson.fromJson(uuidString, String[].class);
		if (array == null)
			return new ArrayList<UUID>();
		final List<UUID> uuids = new ArrayList<UUID>();
		for (final String s : array)
			uuids.add(UUID.fromString(s));
		return uuids;
	}

	@SuppressWarnings("unchecked")
	public static ItemStack fromJsonToItemStack(String s) {
		final Map<String, Object> map = gson.fromJson(s, Map.class);
		ItemStack is;
		try {
			is = ItemStack.deserialize(map);
		} catch (final Exception e) {
			is = new ItemStack(Material.AIR);
			e.printStackTrace();
		}
		return is;
	}

	public static String serializeIntegerArray(int[] integerArray) {
		return gson.toJson(integerArray);
	}

	public static String serializeItems(ItemStack[] items) {
		final List<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		for (final ItemStack item : items) {
			ItemStack is = item;
			if (is == null)
				is = new ItemStack(Material.AIR);
			array.add(is.serialize());
		}
		return gson.toJson(array);
	}

	public static String serializeLocation(Location location) {
		final Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("world", location.getWorld().getName());
		obj.put("x", location.getX());
		obj.put("y", location.getY());
		obj.put("z", location.getZ());
		obj.put("pitch", location.getPitch());
		obj.put("yaw", location.getYaw());
		return gson.toJson(obj);
	}

	public static String serializeStringArray(List<String> stringArray) {
		return gson.toJson(stringArray);
	}

	public static String serializeUUIDList(List<UUID> uuids) {
		final List<String> array = new ArrayList<String>();
		for (final UUID uuid : uuids)
			array.add(uuid.toString());
		return gson.toJson(array);
	}

	public static String toJsonItemStack(ItemStack item) {
		return gson.toJson(item.serialize());
	}

	public static String toJsonList(List<String> list) {
		return gson.toJson(list);
	}

	public static JSONParser parser = new JSONParser();

	public static Gson gson = new Gson();
}