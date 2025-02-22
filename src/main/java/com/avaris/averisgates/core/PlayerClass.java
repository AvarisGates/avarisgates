package com.avaris.averisgates.core;

import com.avaris.averisgates.Averisgates;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Collection;
import java.util.List;

public abstract class PlayerClass {
    public static final AttachmentType<PlayerClassType> PLAYER_CLASS_TYPE_ATTACHMENT= AttachmentRegistry.create(
            Averisgates.id("player_class_type"),
            builder -> builder
                .initializer(() -> PlayerClassType.Warrior) // start with a default value like hunger
                .persistent(PlayerClassType.CODEC) // persist across restarts
                .syncWith(PlayerClassType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );



    public enum PlayerClassType{
        //AVAILABLE IN BETA
        Warrior,
        Archer,
        Mage,
        Priest,

        //In Release
        Rogue,
        Tamer,
        Summoner,
        Necromancer;

        public static final Codec<PlayerClassType> CODEC = new Codec<>() {
            @Override
            public <T> DataResult<Pair<PlayerClassType, T>> decode(DynamicOps<T> ops, T input) {
                return null;
            }

            @Override
            public <T> DataResult<T> encode(PlayerClassType input, DynamicOps<T> ops, T prefix) {
                return null;
            }
        };
        public static final PacketCodec<? super RegistryByteBuf, PlayerClassType> PACKET_CODEC = new PacketCodec<RegistryByteBuf, PlayerClassType>() {
            @Override
            public PlayerClassType decode(RegistryByteBuf buf) {
                return null;
            }

            @Override
            public void encode(RegistryByteBuf buf, PlayerClassType value) {

            }
        };
    }

    private PlayerClassType type;

    public static long MAX_CLASS_LEVEL = 100;

    private long experience; //Class experience
    private long level; //Class levels

    public PlayerClass(PlayerClassType type,long experience){
        this.type = type;
        this.experience = experience;
        this.level = calculateLevel(level);
    }

    //TODO:
    private long calculateLevel(long experience){
        throw new UnsupportedOperationException("Feature incomplete. Contact assistance.");
        // return 0;
    }

    public abstract Collection<PlayerClassAbility> getValidAbilities();

    public abstract Collection<PlayerClassAbility> getLearnedAbilities();

    public abstract Collection<PlayerClassAbility> getActiveAbilities();


}
