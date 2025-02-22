package com.avaris.averisgates.core.network;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.PlayerClassAbility;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CastPlayerClassAbilityC2S(PlayerClassAbility.PlayerClassAbilityType ability) implements CustomPayload {
    public static final PacketCodec<PacketByteBuf, CastPlayerClassAbilityC2S> CODEC;
    public static final Identifier PACKET_ID;
    public static final CustomPayload.Id<CastPlayerClassAbilityC2S> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public CastPlayerClassAbilityC2S decode(PacketByteBuf buf) {
                return new CastPlayerClassAbilityC2S(PlayerClassAbility.PlayerClassAbilityType.fromInt(buf.readInt()));
            }

            @Override
            public void encode(PacketByteBuf buf, CastPlayerClassAbilityC2S value) {
                buf.writeInt(value.ability.ordinal());
            }
        };
        PACKET_ID = Averisgates.id("cast_player_class_ability");
        ID = new CustomPayload.Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
