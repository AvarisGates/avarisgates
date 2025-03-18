package com.avaris.avarisgates.common.player.ability;

import com.avaris.avarisgates.AvarisGates;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;

public enum AbilitySlot {
    SLOT0,
    SLOT1,
    SLOT2,
    BASIC,

    PASSIVE_SLOT0;

    // Call this on startup to make fabric realise that the attachments exist
    public static void init(){

    }
    public AttachmentType<PlayerClassAbilityType> toAttachmentType(){
        switch (this){
            case SLOT0 -> {
                return PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0;
            }
            case SLOT1 -> {
                return PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1;
            }
            case SLOT2 -> {
                return PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2;
            }
            case BASIC -> {
                return PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_BASIC;
            }
            default -> {
                throw new IllegalStateException("Invalid ability type");
            }
        }
    }

    public AttachmentType<Long> toNttAttachment() {
        switch (this){
            case SLOT0 -> {
                return PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0;
            }
            case SLOT1 -> {
                return PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1;
            }
            case SLOT2 -> {
                return PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2;
            }
            case BASIC -> {
                return PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_BASIC;
            }
            default -> {
                throw new IllegalStateException("Invalid ability type");
            }
        }
    }

    private static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_type_0"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    //Next Trigger Time
    private static final AttachmentType<Long> PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_ntt_0"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    private static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_type_1"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    //Next Trigger Time
    private static final AttachmentType<Long> PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_ntt_1"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    private static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_type_2"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    //Next Trigger Time
    private static final AttachmentType<Long> PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2 = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_ntt_2"),
            builder -> builder
                    .initializer(() -> 2L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    private static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_BASIC = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_type_basic"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    //Next Trigger Time
    private static final AttachmentType<Long> PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_BASIC = AttachmentRegistry.create(
            AvarisGates.id("player_class_ability_ntt_basic"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );
}
