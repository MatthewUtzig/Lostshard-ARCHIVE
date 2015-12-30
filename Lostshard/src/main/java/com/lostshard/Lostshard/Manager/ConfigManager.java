package com.lostshard.Lostshard.Manager;

import org.bukkit.configuration.file.FileConfiguration;

import com.lostshard.Lostshard.Data.Locations;
import com.lostshard.Lostshard.Data.Variables;
import com.lostshard.Lostshard.Handlers.DamageHandler;
import com.lostshard.Lostshard.Main.Lostshard;
import com.lostshard.Lostshard.Utils.Serializer;

public class ConfigManager {

	public static ConfigManager getManager() {
		return manager;
	}

	static ConfigManager manager = new ConfigManager();
	FileConfiguration config;

	Lostshard plugin;

	public ConfigManager() {
	}

	public void reload() {
		this.plugin.reloadConfig();
		this.config = this.plugin.getConfig();
		Variables.mysqlUrl = this.config.getString("MYSQL.url");
		Variables.mysqlUsername = this.config.getString("MYSQL.username");
		Variables.mysqlPassword = this.config.getString("MYSQL.password");

		Variables.motd = this.config.getString("GENERAL.motd");

		DamageHandler.base = this.config.getDouble("DAMAGE.base");
		DamageHandler.armor = this.config.getDouble("DAMAGE.armor");
		DamageHandler.magic = this.config.getDouble("DAMAGE.magic");
		DamageHandler.resistance = this.config.getDouble("DAMAGE.resistance");
		DamageHandler.hardhat = this.config.getDouble("DAMAGE.hardhat");
		DamageHandler.arrow = this.config.getDouble("DAMAGE.arrow");
		DamageHandler.hand = this.config.getDouble("DAMAGE.hand");
		DamageHandler.swords = this.config.getDouble("DAMAGE.swords");
		DamageHandler.diamondSword = this.config
				.getDouble("DAMAGE.diamondSword");
		DamageHandler.ironSword = this.config.getDouble("DAMAGE.ironSword");
		DamageHandler.goldSword = this.config.getDouble("DAMAGE.goldSword");
		DamageHandler.stoneSword = this.config.getDouble("DAMAGE.stoneSword");
		DamageHandler.woodSword = this.config.getDouble("DAMAGE.woodSword");

		Locations.LAWFULL
				.setLocation(Serializer.deserializeLocation(this.config
						.getString("LOCATIONS.lawfull")));
		Locations.CRIMINAL.setLocation(Serializer
				.deserializeLocation(this.config
						.getString("LOCATIONS.criminal")));
		Locations.BUILDCHANGLAWFULL.setLocation(Serializer
				.deserializeLocation(this.config
						.getString("LOCATIONS.build.lawfull")));
		Locations.BUILDCHANGECRIMINAL.setLocation(Serializer
				.deserializeLocation(this.config
						.getString("LOCATIONS.build.criminal")));
	}

	public void setConfig(Lostshard plugin) {
		this.plugin = plugin;
		this.reload();
	}
}
