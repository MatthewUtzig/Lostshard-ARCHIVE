package com.lostshard.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import com.lostshard.Main.Lostshard;

public class Serializer {

	public static Location deserializeLocation(String locationString) {
		Location deLoc = null;
		try {
			String[] locationList = locationString.split(",");
			deLoc = new Location(Bukkit.getWorld(locationList[0]), Double.parseDouble(locationList[1]),
					Double.parseDouble(locationList[2]), Double.parseDouble(locationList[3]), 
					Float.parseFloat(locationList[4]), Float.parseFloat(locationList[5]));
		} catch(Exception e) {
			Lostshard.logger.log(Level.WARNING, "[SERIALIZER] Exception while serializing a location.");
		}
		return deLoc;
	}
	
	public static String serializeLocation(Location location) {
		String locString = location.getWorld().getName()+","+location.getX()+","+
	location.getY()+","+location.getZ()+","+location.getPitch()+","+location.getYaw();
		return locString;
	}
	
	public static List<UUID> deserializeUUIDList(String uuidString) {
		String[] uuidList = uuidString.split(",");
		List<UUID> uuids = new ArrayList<UUID>();
		for(String uuid : uuidList)
			uuids.add(UUID.fromString(uuid));
		return  uuids;
	}
	
	public static String serializeUUIDList(List<UUID> uuids) {
		String uuidString = "";
		for(UUID uuid : uuids)
			if(uuid == uuids.get(uuids.size()-1))
				uuidString += uuid.toString();
			else
				uuidString += uuid.toString()+",";
		return uuidString;
	}
}
