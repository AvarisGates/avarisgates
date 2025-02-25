package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
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

    protected AttachedAbility ability;

    public PlayerClassAbility(AttachedAbility ability) {
        this.nextTriggerTime = ability.getNtt();
        this.ability = ability;
    }


    public static PlayerClassAbility build(AttachedAbility ability) {
        switch (ability.getType()){
            case Teleport -> {
                return new TeleportAbility(ability);
            }
            case Cleave -> {
                return new CleaveAbility(ability);
            }
            case Whirlwind -> {
                return new WhirlwindAbility(ability);
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
        this.ability.setNtt(this.nextTriggerTime);
        AttachedAbility.setAttached(player,this.ability);
        AvarisGates.LOGGER.info("{} triggered",this.getAbilityType());
    }
}
