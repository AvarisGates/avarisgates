package com.avaris.averisgates.core;

import com.avaris.averisgates.Averisgates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerClassAbility {
    protected long minLevel;
    protected long nextTriggerTime; //In ticks

    public static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0 = AttachmentRegistry.create(
            Averisgates.id("player_class_ability_type_0"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    public enum PlayerClassAbilityType{
        Swing,
        Cleave,
        Teleport
        ;

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
                default -> throw new IllegalStateException("Unexpected ability type value: " + i);
            }
        }
    }

    public abstract PlayerClassAbilityType getAbilityType();

    public abstract PlayerClassType getClassType();

    public long getMinLevel(){
        return this.minLevel;
    }

    public abstract long getBaseCooldown();

    public long getNextTriggerTime(){
        return nextTriggerTime;
    }

    public long getCooldown(long time){
       return Math.max(0, nextTriggerTime - time);
    }

    public void trigger(MinecraftServer server, ServerPlayerEntity player){
        this.nextTriggerTime = server.getTicks() + this.getBaseCooldown();
    }
}
