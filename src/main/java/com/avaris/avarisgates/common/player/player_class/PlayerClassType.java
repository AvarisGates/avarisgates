package com.avaris.avarisgates.common.player.player_class;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;

public enum PlayerClassType {
    //AVAILABLE IN BETA
    Beginner,
    Warrior,
    Archer,
    Mage,
    Priest,

    //In Release
    Rogue,
    Tamer,
    Summoner,
    Necromancer;

    private static PlayerClassType fromInt(int i) {
        return switch (i) {
            case 0 -> Beginner;
            case 1 -> Warrior;
            case 2 -> Archer;
            case 3 -> Mage;
            case 4 -> Priest;

            default -> throw new IllegalStateException("Invalid payer class type: " + i);
        };
    }

    public static final Codec<PlayerClassType> CODEC = new PrimitiveCodec<>() {
        @Override
        public <T> DataResult<PlayerClassType> read(DynamicOps<T> ops, T input) {
            return ops
                    .getNumberValue(input)
                    .map(Number::intValue)
                    .map(PlayerClassType::fromInt);
        }

        @Override
        public <T> T write(DynamicOps<T> ops, final PlayerClassType value) {
            return ops.createInt(value.ordinal());
        }
    };
    public static final PacketCodec<? super RegistryByteBuf, PlayerClassType> PACKET_CODEC = new PacketCodec<RegistryByteBuf, PlayerClassType>() {
        @Override
        public PlayerClassType decode(RegistryByteBuf buf) {
            return PlayerClassType.fromInt(buf.readInt());
        }

        @Override
        public void encode(RegistryByteBuf buf, PlayerClassType value) {
            buf.writeInt(value.ordinal());
        }
    };

}
