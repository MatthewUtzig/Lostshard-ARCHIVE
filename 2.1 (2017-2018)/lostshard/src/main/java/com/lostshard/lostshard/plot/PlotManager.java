package com.lostshard.lostshard.plot;

import com.lostshard.lostshard.main.Manager;
import com.lostshard.lostshard.main.ServiceStartException;
import com.lostshard.lostshard.main.ServiceStopException;
import com.mongodb.Function;
import com.mongodb.async.client.MongoDatabase;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.util.function.Consumer;

public class PlotManager implements Manager<PlotManager> {

    private final Plugin plugin;
    private PlotRepository plotRepository;

    public PlotManager(Plugin plugin, MongoDatabase database) {
        this.plugin = plugin;
        this.plotRepository = new PlotRepository(database.getCollection("plots", Plot.class));
    }

    @Override
    public boolean enable(Consumer<PlotManager> consumer) {
       plotRepository.load(stringPlotMap -> {
           consumer.accept(this);
       });
    }

    @Override
    public boolean disable() throws ServiceStopException {

    }

    public void findPlotAt(Location location, Function<Plot, Void> function) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (Plot plot : plotRepository.getPlots().values())
                if (location.distance(plot.getLocation()) <= plot.getSize()) {
                    function.apply(plot);
                    break;
                }
        });
    }
}
