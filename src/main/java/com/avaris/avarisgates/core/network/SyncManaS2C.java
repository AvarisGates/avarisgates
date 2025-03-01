package com.avaris.avarisgates.core.network;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.ManaAttachment;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SyncManaS2C(ManaAttachment manaAttachment) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, SyncManaS2C> CODEC;
    public static final Identifier PACKET_ID;
    public static final Id<SyncManaS2C> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public SyncManaS2C decode(PacketByteBuf buf) {
                return new SyncManaS2C(new ManaAttachment(buf.readLong(),buf.readLong()));
            }

            @Override
            public void encode(PacketByteBuf buf, SyncManaS2C value) {
                buf.writeLong(value.manaAttachment.getValue());
                buf.writeLong(value.manaAttachment().getMaxValue());
            }
        };
        PACKET_ID = AvarisGates.id("sync_mana");
        ID = new Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
