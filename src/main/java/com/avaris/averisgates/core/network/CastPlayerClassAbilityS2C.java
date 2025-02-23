package com.avaris.averisgates.core.network;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.player.ability.PlayerClassAbilityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

//The server should respond with the result, and the ability type
//Also see CastPlayerClassAbilityC2S
public record CastPlayerClassAbilityS2C(PlayerClassAbilityType ability) implements CustomPayload {

    public static final PacketCodec<PacketByteBuf, CastPlayerClassAbilityS2C> CODEC;
    public static final Identifier PACKET_ID;
    public static final CustomPayload.Id<CastPlayerClassAbilityS2C> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public CastPlayerClassAbilityS2C decode(PacketByteBuf buf) {
                return new CastPlayerClassAbilityS2C(PlayerClassAbilityType.fromInt(buf.readInt()));
            }

            @Override
            public void encode(PacketByteBuf buf, CastPlayerClassAbilityS2C value) {
                buf.writeInt(value.ability.ordinal());
            }
        };
        PACKET_ID = Averisgates.id("cast_player_class_ability_response");
        ID = new CustomPayload.Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
