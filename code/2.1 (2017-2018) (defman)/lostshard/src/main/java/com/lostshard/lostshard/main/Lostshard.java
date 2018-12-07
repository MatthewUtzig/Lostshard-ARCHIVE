package com.lostshard.lostshard.main;

import com.lostshard.lostshard.player.PlayerManager;
import com.lostshard.lostshard.plot.PlotManager;
import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClientSettings;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ServerSettings;
import com.sun.scenario.Settings;
import org.bukkit.plugin.java.JavaPlugin;

import com.lostshard.lostshard.Manager.ClanManager;
import com.lostshard.lostshard.Spells.MagicStructure;
import com.lostshard.lostshard.Utils.ItemUtils;

public class Lostshard extends JavaPlugin {

    private MongoClient client;
    private MongoDatabase database;
    private PlayerManager pm;
    private PlotManager ptm;
    private ClanManager cm;

    @Override
    public void onEnable() {
        Settings settings = Configurator.get(Settings.class);

        this.client = MongoClients.create(
                MongoClientSettings.builder()
                        .applicationName("Lostshard")
                        .serverSettings(ServerSettings.builder()
                                .applyConnectionString(new ConnectionString(Settings.mongodb)).build())
                .build());
        this.database = client.getDatabase(Settings.database);

        PlotManager plotManager = new PlotManager(this, database);
        PlayerManager playerManager = new PlayerManager(this, database);
        ClanManager clanManager = new ClanManager(this, database);

        try {
            playerManager.enable();
            plotManager.enable();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ItemUtils.addChainMail();
    }

    @Override
    public void onDisable() {

        MagicStructure.removeAll();
    }
}
