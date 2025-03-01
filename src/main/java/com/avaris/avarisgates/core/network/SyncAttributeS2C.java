package com.avaris.avarisgates.core.network;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncAttributeS2C(AttributeType type, long value) implements CustomPayload{
    public static final PacketCodec<PacketByteBuf, SyncAttributeS2C> CODEC;
    public static final Identifier PACKET_ID;
    public static final Id<SyncAttributeS2C> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public SyncAttributeS2C decode(PacketByteBuf buf) {
                return new SyncAttributeS2C(AttributeType.fromInt(buf.readInt()),buf.readLong());
            }

            @Override
            public void encode(PacketByteBuf buf, SyncAttributeS2C value) {
               buf.writeInt(value.type.ordinal());
                buf.writeLong(value.value);
            }
        };
        PACKET_ID = AvarisGates.id("attribute_increment");
        ID = new Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
        }
}
