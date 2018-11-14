package com.lostshard.lostshard.player;

import com.lostshard.lostshard.main.Repository;
import com.mongodb.async.client.MongoCollection;
import org.bson.BsonDocument;
import org.bson.Document;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;

public class PlayerRepository implements Repository {

    private final MongoCollection<LSPlayer> playerCollection;

    public PlayerRepository(MongoCollection<LSPlayer> playerCollection) {
        this.playerCollection = playerCollection;
    }

    public void load(Runnable runnable, UUID...uuids) {
        playerCollection
                .find(new BsonDocument("uuid", new BsonDocument("$in", Arrays.stream(uuids))))
                .into(new HashSet<>(), (result, t) -> {
                    runnable.run();
                });
    }

}
