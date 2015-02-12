package com.lostshard.lostshard.Manager;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

	static ConfigManager manager = new ConfigManager();
	
	FileConfiguration config;
	
	public ConfigManager() {
	}

	public static ConfigManager getManager() {
		return manager;
	}
	
	public String getMYSQLUsername() {
		return config.getString("MYSQL.username");
	}
	
	public String getMYSQLPassword() {
		return config.getString("MYSQL.password");
	}
	
	public String getMYSQLUrl() {
		return config.getString("MYSQL.url");
	}

	public String getMYSQLDriver() {
		return config.getString("MYSQL.driver");
	}
	
	public void setConfig(FileConfiguration config) {
		manager.config = config;
	}
	
}
