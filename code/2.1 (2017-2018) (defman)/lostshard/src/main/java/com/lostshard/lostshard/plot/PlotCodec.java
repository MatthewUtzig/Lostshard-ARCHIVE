package com.lostshard.lostshard.plot;

import com.lostshard.lostshard.main.LocationCodec;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.UuidRepresentation;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.UuidCodec;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PlotCodec implements Codec<Plot> {

    private final UuidCodec uuidCodec;
    private final LocationCodec locationCodec;

    public PlotCodec() {
        this.uuidCodec = new UuidCodec(UuidRepresentation.STANDARD);
        this.locationCodec = new LocationCodec(true, false);
    }

    @Override
    public Plot decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String name = reader.readName();
        UUID owner = uuidCodec.decode(reader, decoderContext);
        Location location = locationCodec.decode(reader, decoderContext);
        int size = reader.readInt32();

        Set<PlotUpgrade> plotUpgrades = new HashSet<>();

        reader.readStartArray();
            if (!reader.readBsonType().equals(BsonType.END_OF_DOCUMENT))
                plotUpgrades.add(PlotUpgrade.valueOf(reader.readString()));
        reader.readEndArray();

        Set<PlotFlag> plotFlags = new HashSet<>();

        reader.readStartArray();
        if (!reader.readBsonType().equals(BsonType.END_OF_DOCUMENT))
            plotFlags.add(PlotFlag.valueOf(reader.readString()));
        reader.readEndArray();

        reader.readEndDocument();
        return null;
    }

    @Override
    public void encode(BsonWriter writer, Plot value, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeString("name", value.getName());
        encoderContext.encodeWithChildContext(uuidCodec, writer, value.getOwner());
        encoderContext.encodeWithChildContext(locationCodec, writer, value.getLocation());
        writer.writeInt32("size", value.getSize());

        writer.writeStartArray("upgrades");
            for (PlotUpgrade plotUpgrade : value.getUpgrades()) {
                writer.writeString(plotUpgrade.name());
            }
        writer.writeEndArray();

        writer.writeStartArray("flags");
            for (PlotFlag flag : value.getFlags()) {
                writer.writeString(flag.name());
            }
        writer.writeEndArray();


        writer.writeEndDocument();
    }

    @Override
    public Class<Plot> getEncoderClass() {
        return null;
    }
}
