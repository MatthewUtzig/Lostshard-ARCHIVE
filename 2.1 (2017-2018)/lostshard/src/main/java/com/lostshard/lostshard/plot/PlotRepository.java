package com.lostshard.lostshard.plot;

import com.lostshard.lostshard.main.Repository;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.BsonDocument;
import org.bson.BsonString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PlotRepository implements Repository {

    private final MongoCollection<Plot> plotCollection;
    private Map<String, Plot> plots;

    public PlotRepository(MongoCollection<Plot> plotCollection) {
        this.plotCollection = plotCollection;
        this.plots = new ConcurrentHashMap<>();
    }

    public MongoCollection<Plot> getPlotCollection() {
        return plotCollection;
    }

    public Map<String, Plot> getPlots() {
        return plots;
    }

    public synchronized void setPlots(Map<String, Plot> plots) {
        this.plots = plots;
    }

    public void load(Consumer<Map<String, Plot>> consumer) {
        plotCollection.find().into(new HashSet<>(), (result, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }
            Map<String, Plot> plots = new ConcurrentHashMap<String, Plot>(result.size());
            result.forEach(plot -> plots.put(plot.getName(), plot));
            this.setPlots(plots);
            consumer.accept(plots);
        });
    }

    public void load(Consumer<Plot> consumer, String name) {
        fetch(plot -> {
            this.plots.put(plot.getName(), plot);
            consumer.accept(plot);
        }, name);
    }

    public void persist(Runnable runnable, Plot...plots) {
        plotCollection.bulkWrite(Arrays.stream(plots).map(plot -> new ReplaceOneModel<Plot>(new BsonDocument("name",
                new BsonString(plot.getName())), plot, new UpdateOptions().upsert(true))).collect(Collectors.toList()
        ), (result, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }
            runnable.run();
        });
    }

    public void insert(Runnable runnable, Plot plot) {
        plotCollection.insertOne(plot, (result, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }
            runnable.run();
        });
    }

    public void fetch(Consumer<Plot> consumer, String name) {
        plotCollection.find(new BsonDocument("name", new BsonString(name))).first((result, t) -> {
            if (t != null) {
                t.printStackTrace();
                return;
            }
            plots.put(result.getName(), result);
            consumer.accept(result);
        });
    }

}
