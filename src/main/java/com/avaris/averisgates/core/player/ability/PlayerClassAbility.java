package com.avaris.averisgates.core.player.ability;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.player.player_class.PlayerClassType;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public abstract class PlayerClassAbility {
    protected long minLevel;
    protected long nextTriggerTime; //In ticks
    protected AttachmentType<Long> slot;

    public static final AttachmentType<PlayerClassAbilityType> PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0 = AttachmentRegistry.create(
            Averisgates.id("player_class_ability_type_0"),
            builder -> builder
                    .initializer(() -> PlayerClassAbilityType.Cleave) // start with a default value like hunger
                    .persistent(PlayerClassAbilityType.CODEC) // persist across restarts
                    .syncWith(PlayerClassAbilityType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    //Next Trigger Time
    public static final AttachmentType<Long> PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0 = AttachmentRegistry.create(
            Averisgates.id("player_class_ability_ntt_0"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    public static PlayerClassAbility build(PlayerClassAbilityType newType, Long ntt, AttachmentType<Long> slot) {
        switch (newType){
            case Teleport -> {
                return new TeleportAbility(ntt,slot);
            }
        }
        return null;
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
        player.setAttached(slot,this.nextTriggerTime);
    }
}
