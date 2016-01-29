package com.lostshard.Lostshard.Utils;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;

public class Serializer {

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
	
	public static String serializeItemStack(ItemStack item) {
		if(item == null)
			item = new ItemStack(Material.AIR);
		return gson.toJson(item.serialize());
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack deserializeItemStack(String item) {
		if(item == null)
			return new ItemStack(Material.AIR);
		return ItemStack.deserialize(gson.fromJson(item, Map.class));
	}
	
	public static String serializeContents(ItemStack[] items) {
		String[] result = new String[items.length];
			for(int i=0; i<items.length; i++) {
				ItemStack item = items[i];
				if(item == null)
					item = new ItemStack(Material.AIR);
				result[i] = serializeItemStack(item);
			}	
		return gson.toJson(result);
	}
	
	public static ItemStack[] deserializeContents(String items) {
		String[] list = gson.fromJson(items, String[].class);
		ItemStack[] result = new ItemStack[list.length];
		for(int i=0; i<list.length; i++)
			result[i] = deserializeItemStack(list[i]);
		return result;
	}
	
	public static JSONParser parser = new JSONParser();

	public static Gson gson = new Gson();
}