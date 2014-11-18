package com.lostshard.lostshard.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.lostshard.lostshard.Main.Lostshard;

public class Serializer {

	static JSONParser parser = new JSONParser();
	static Gson gson = new Gson();

	public static Location deserializeLocation(String locationString) {
		try {
			Object jo = parser.parse(locationString);
			JSONObject map = (JSONObject) jo;

			World world = Bukkit.getWorld(map.get("world").toString());
			Double x = ((Number) map.get("x")).doubleValue();
			Double y = ((Number) map.get("y")).doubleValue();
			Double z = ((Number) map.get("z")).doubleValue();
			Float yaw = ((Number) map.get("yaw")).floatValue();
			Float pitch = ((Number) map.get("pitch")).floatValue();

			return new Location(world, x, y, z, yaw, pitch);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String serializeLocation(Location location) {
		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("world", location.getWorld().getName());
		obj.put("x", location.getX());
		obj.put("y", location.getY());
		obj.put("z", location.getZ());
		obj.put("pitch", location.getPitch());
		obj.put("yaw", location.getYaw());
		return gson.toJson(obj);
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

	public static String serializeInventory(Inventory inv) {
		List<String> array = new ArrayList<String>();
		for (int i = 0; i < inv.getContents().length; i++) {
			ItemStack is = inv.getContents()[i];
			if (is == null)
				is = new ItemStack(Material.AIR);
			array.add(gson.toJson(is.serialize()));
		}
		return gson.toJson(array);
	}

	@SuppressWarnings("unchecked")
	public static ItemStack[] deserializeItems(String string) {
		ItemStack[] rs;
		try {
			List<String> stacks = (ArrayList<String>) gson.fromJson(string,
					List.class);
			rs = new ItemStack[stacks.size()];
			for (int i = 0; i < stacks.size(); i++) {
				String s = (String) stacks.get(i);

				Map<String, Object> map = (Map<String, Object>) gson.fromJson(
						s, Map.class);
				for (Entry<String, Object> k : map.entrySet())
					if (k.getValue() instanceof Number)
						k.setValue(((Number) k.getValue()).intValue());
				ItemStack is;
				try {
					is = ItemStack.deserialize(map);
				} catch (Exception e) {
					is = new ItemStack(Material.AIR);
					e.printStackTrace();
				}
				rs[i] = is;
			}
			return rs;
		} catch (Exception e) {
			Lostshard.logger.log(Level.WARNING, "[Inventory-Serialization] "
					+ string);
		}
		rs = new ItemStack[] { new ItemStack(Material.AIR) };
		return rs;
	}
}
