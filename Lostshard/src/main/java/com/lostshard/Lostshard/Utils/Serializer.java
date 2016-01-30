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
		try {
			if(item == null)
				item = new ItemStack(Material.AIR);
			return gson.toJson(item.serialize());
		} catch(Exception e) {
			return gson.toJson(new ItemStack(Material.AIR).serialize());
		}
	}
	
	@SuppressWarnings("unchecked")
	public static ItemStack deserializeItemStack(String item) {
		try {
			if(item == null)
				return new ItemStack(Material.AIR);
			Map<String, Object> map = gson.fromJson(item, Map.class);
			return ItemStack.deserialize(map);
		} catch(Exception e) {
			ItemStack result = new ItemStack(Material.COBBLESTONE);
			result.getItemMeta().setDisplayName(ChatColor.RED+"ERROR!");
			return result;
		}
	}
	
	public static String serializeContents(ItemStack[] items) {
		try {
		String[] result = new String[items.length];
		for(int i=0; i<items.length; i++) {
			ItemStack item = items[i];
			if(item == null)
				item = new ItemStack(Material.AIR);
			result[i] = itemTo64(item);
		}
			return gson.toJson(result);
		} catch(Exception e) {
			return "";
		}
	}
	
	public static ItemStack[] deserializeContents(String items) {
		try {
			String[] list = gson.fromJson(items, String[].class);
			ItemStack[] result = new ItemStack[list.length];
			for(int i=0; i<list.length; i++)
				result[i] = itemFrom64(list[i]);
			return result;
		} catch(Exception e) {
			return null;
		}
	}
	
	public static String itemTo64(ItemStack stack) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(stack);

            // Serialize that array
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        }
        catch (Exception e) {
        	e.printStackTrace();
            return "";
        }
    }
   
    public static ItemStack itemFrom64(String data) {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            try {
                return (ItemStack) dataInput.readObject();
            } finally {
                dataInput.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(Material.AIR);
        }
    }
	
	public static JSONParser parser = new JSONParser();

	public static Gson gson = new Gson();
}