package com.avaris.avarisgates.core.network;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record RequestAttributeIncrementC2S(AttributeType type) implements CustomPayload{
    public static final PacketCodec<PacketByteBuf, RequestAttributeIncrementC2S> CODEC;
    public static final Identifier PACKET_ID;
    public static final CustomPayload.Id<RequestAttributeIncrementC2S> ID;

    static {
        CODEC = new PacketCodec<>() {
            @Override
            public RequestAttributeIncrementC2S decode(PacketByteBuf buf) {
                return new RequestAttributeIncrementC2S(AttributeType.fromInt(buf.readInt()));
            }

            @Override
            public void encode(PacketByteBuf buf, RequestAttributeIncrementC2S value) {
               buf.writeInt(value.type.ordinal());
            }
        };
        PACKET_ID = AvarisGates.id("request_attribute_increment");
        ID = new CustomPayload.Id<>(PACKET_ID);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
        }
}
