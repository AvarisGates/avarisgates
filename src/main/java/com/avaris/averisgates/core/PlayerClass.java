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
import net.minecraft.network.codec.PacketCodecs;

import java.util.Collection;

public abstract class PlayerClass {
    public static final AttachmentType<PlayerClassType> PLAYER_CLASS_TYPE_ATTACHMENT = AttachmentRegistry.create(
            Averisgates.id("player_class_type"),
            builder -> builder
                .initializer(() -> PlayerClassType.Warrior) // start with a default value like hunger
                .persistent(PlayerClassType.CODEC) // persist across restarts
                .syncWith(PlayerClassType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );


    public static final AttachmentType<Long> PLAYER_EXPERIENCE_ATTACHMENT = AttachmentRegistry.create(
            Averisgates.id("player_experience"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
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

        private static PlayerClassType fromInt(int i) {
            return switch (i) {
                case 0 -> Warrior;
                case 1 -> Archer;
                case 2 -> Mage;
                case 3 -> Priest;

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
            public <T> T write(DynamicOps<T> ops,final PlayerClassType value) {
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

    private PlayerClassType type;

    public static long MAX_CLASS_LEVEL = 100;

    private long experience; //Class experience
    private long level; //Class levels

    public PlayerClass(PlayerClassType type,long experience){
        this.type = type;
        this.experience = experience;
        this.level = calculateLevel(level,type);
    }
    
    private long calculateLevel(long experience, PlayerClassType playerclassthing) {
        long baseXp = 100;  // Base XP requirement for level 1
        double multiplier;

        // We gotta retrieve the class rarity but idk how to do it
        // String rarity = playerclassthing.getRarity();

        // PLACEHOLDER
        String rarity = "COMMON"; // Replace this with the actual retrieval from playerclassthing

        switch (rarity.toUpperCase()) {
            case "COMMON":
                multiplier = 1.0;
                break;
            case "UNCOMMON":
                multiplier = 1.5;
                break;
            case "RARE":
                multiplier = 3.0;
                break;
            case "EPIC":
                multiplier = 6.0;
                break;
            case "LEGENDARY":
                multiplier = 12.0;
                break;
            default:
                throw new IllegalArgumentException("Invalid rarity. Choose from COMMON, UNCOMMON, RARE, EPIC, LEGENDARY.");
        }

        int level = 0;
        long totalXp = 0;

        while (totalXp <= experience && level < 101) {
            level++;
            totalXp += baseXp * Math.pow(level, 1.005) * multiplier;
            totalXp = (totalXp / 100) * 100;  // Round down to the nearest multiple of 100
        }

        return level - 1;
    }

    public abstract Collection<PlayerClassAbility> getValidAbilities();

    public abstract Collection<PlayerClassAbility> getLearnedAbilities();

    public abstract Collection<PlayerClassAbility> getActiveAbilities();


}
