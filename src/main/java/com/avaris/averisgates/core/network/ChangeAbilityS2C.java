package com.avaris.averisgates.core.network;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.player.ability.PlayerClassAbilityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ChangeAbilityS2C(int slot,PlayerClassAbilityType ability) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, ChangeAbilityS2C> CODEC;
    public static final Identifier PACKET_ID;
    public static final CustomPayload.Id<ChangeAbilityS2C> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public ChangeAbilityS2C decode(PacketByteBuf buf) {
                return new ChangeAbilityS2C(buf.readInt(),PlayerClassAbilityType.fromInt(buf.readInt()));
            }

            @Override
            public void encode(PacketByteBuf buf, ChangeAbilityS2C value) {
                buf.writeInt(value.slot);
                buf.writeInt(value.ability.ordinal());
            }
        };
        PACKET_ID = Averisgates.id("change_ability");
        ID = new CustomPayload.Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
