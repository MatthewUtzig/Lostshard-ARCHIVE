package com.lostshard.lostshard.main;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.UuidRepresentation;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.UuidCodec;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

public class LocationCodec implements Codec<Location> {

    private final boolean numeric;
    private final boolean directional;
    private final UuidCodec uuidCodec;

    public LocationCodec(boolean numeric, boolean directional) {
        this.numeric = numeric;
        this.directional = directional;
        this.uuidCodec = new UuidCodec(UuidRepresentation.STANDARD);
    }

    @Override
    public Location decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        UUID worldUID = uuidCodec.decode(reader, decoderContext);

        double x = reader.readDouble();
        double y = reader.readDouble();
        double z = reader.readDouble();

        float yaw = 0;
        float pitch = 0;

        if (reader.readName().equals("yaw"))
            yaw = (float) reader.readDouble();
        if (reader.readName().equals("pitch"))
            pitch = (float) reader.readDouble();

        reader.readEndDocument();

        World world = Bukkit.getWorld(worldUID);
        if(world == null)
            return null;

        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public void encode(BsonWriter writer, Location value, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("world");
        encoderContext.encodeWithChildContext(uuidCodec, writer, value.getWorld().getUID());

        if (numeric) {
            writer.writeInt32("x", value.getBlockX());
            writer.writeInt32("y", value.getBlockY());
            writer.writeInt32("z", value.getBlockZ());
        } else {
            writer.writeDouble("x", value.getX());
            writer.writeDouble("y", value.getY());
            writer.writeDouble("z", value.getZ());
        }

        if (directional) {
            writer.writeDouble("yaw", value.getYaw());
            writer.writeDouble("pitch", value.getPitch());
        }

        writer.writeEndDocument();
    }

    @Override
    public Class<Location> getEncoderClass() {
        return Location.class;
    }
}
