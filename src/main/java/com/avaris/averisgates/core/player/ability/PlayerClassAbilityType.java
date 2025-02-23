package com.avaris.averisgates.core.player.ability;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum PlayerClassAbilityType {
    Swing,
    Cleave,
    Teleport,
    Whirlwind;

    public static final Codec<PlayerClassAbilityType> CODEC = new PrimitiveCodec<PlayerClassAbilityType>() {
        @Override
        public <T> DataResult<PlayerClassAbilityType> read(DynamicOps<T> ops, T input) {
            return ops.getNumberValue(input)
                    .map(Number::intValue)
                    .map(PlayerClassAbilityType::fromInt);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, PlayerClassAbilityType value) {
            return ops.createInt(value.ordinal());
        }
    };

    public static final PacketCodec<? super RegistryByteBuf, PlayerClassAbilityType> PACKET_CODEC = new PacketCodec<RegistryByteBuf, PlayerClassAbilityType>() {
        @Override
        public PlayerClassAbilityType decode(RegistryByteBuf buf) {
            return PlayerClassAbilityType.fromInt(buf.readInt());
        }

        @Override
        public void encode(RegistryByteBuf buf, PlayerClassAbilityType value) {
            buf.writeInt(value.ordinal());
        }
    };

    public static PlayerClassAbilityType fromInt(int i) {
        switch (i) {
            case 0 -> {
                return Swing;
            }
            case 1 -> {
                return Cleave;
            }
            case 2 -> {
                return Teleport;
            }
            case 3 -> {
                return Whirlwind;
            }
            default -> throw new IllegalStateException("Unexpected ability type value: " + i);
        }
    }
}
