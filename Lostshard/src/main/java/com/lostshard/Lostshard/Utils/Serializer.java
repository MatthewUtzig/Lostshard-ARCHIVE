package com.lostshard.Lostshard.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import com.google.gson.Gson;

import net.md_5.bungee.api.ChatColor;

public class Serializer {

	public static JSONParser parser = new JSONParser();

	public static Gson gson = new Gson();

	public static ItemStack[] deserializeContents(String items) {
		try {
			final String[] list = gson.fromJson(items, String[].class);
			final ItemStack[] result = new ItemStack[list.length];
			for (int i = 0; i < list.length; i++)
				result[i] = itemFrom64(list[i]);
			return result;
		} catch (final Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static ItemStack deserializeItemStack(String item) {
		try {
			if (item == null)
				return new ItemStack(Material.AIR);
			final Map<String, Object> map = gson.fromJson(item, Map.class);
			return ItemStack.deserialize(map);
		} catch (final Exception e) {
			final ItemStack result = new ItemStack(Material.COBBLESTONE);
			result.getItemMeta().setDisplayName(ChatColor.RED + "ERROR!");
			return result;
		}
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

	public static ItemStack itemFrom64(String data) {
		try {
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			final BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			try {
				return (ItemStack) dataInput.readObject();
			} finally {
				dataInput.close();
			}
		} catch (final Exception e) {
			e.printStackTrace();
			return new ItemStack(Material.AIR);
		}
	}

	public static String itemTo64(ItemStack stack) {
		try {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			dataOutput.writeObject(stack);

			// Serialize that array
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (final Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static String serializeContents(ItemStack[] items) {
		try {
			final String[] result = new String[items.length];
			for (int i = 0; i < items.length; i++) {
				ItemStack item = items[i];
				if (item == null)
					item = new ItemStack(Material.AIR);
				result[i] = itemTo64(item);
			}
			return gson.toJson(result);
		} catch (final Exception e) {
			return "";
		}
	}

	public static String serializeItemStack(ItemStack item) {
		try {
			if (item == null)
				item = new ItemStack(Material.AIR);
			return gson.toJson(item.serialize());
		} catch (final Exception e) {
			return gson.toJson(new ItemStack(Material.AIR).serialize());
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
}