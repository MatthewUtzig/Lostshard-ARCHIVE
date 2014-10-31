package com.lostshard.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Location;

import com.lostshard.Main.Lostshard;
import com.lostshard.Objects.Plot;
import com.lostshard.Utils.Serializer;

public class Database {

	protected static ConnectionPool connPool = new ConnectionPool();
	
	public static void getPlots() {
		try {
			Connection conn = connPool.getConnection();
			PreparedStatement prep = conn.prepareStatement("SELECT * FROM plots");
			prep.execute();
			ResultSet rs = prep.getResultSet();
			ArrayList<Plot> plots = new ArrayList<Plot>();
			while(rs.next()) {
				String name = rs.getString("name");
				try {
					int id = rs.getInt("id");
					//Location
					Location location = Serializer.deserializeLocation(rs.getString("location"));
					//Upgrades
					boolean town = rs.getBoolean("town");
					boolean dungeon = rs.getBoolean("dungeon");
					boolean neutralAlignment = rs.getBoolean("neutralalignment");
					boolean autoKick = rs.getBoolean("autokick");
					//Admin stuff
					boolean capturepoint = rs.getBoolean("capturepoint");
					boolean magic = rs.getBoolean("magic");
					boolean pvp = rs.getBoolean("pvp");
					//Friend and co-owners and owner
					
				} catch(Exception e) {
					Lostshard.logger.log(Level.WARNING, "[PLOT] Exception when generating \""+name+"\" plot: "+e.toString());
				}
			}
		} catch(Exception e) {
			Lostshard.logger.log(Level.WARNING, "[PLOT] getPlots mysql error >> "+e.toString());
		}
	}
	
}
