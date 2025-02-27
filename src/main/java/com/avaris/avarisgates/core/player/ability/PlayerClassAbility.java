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

import java.lang.reflect.InvocationTargetException;

// To add a new ability:
// 1. Create a new ability type in PlayerClassAbilityType
// 2. Register it in the AbilityRegistrar
// 3. Create a new ability class extending from this class, make sure to call super.trigger() at the end of the trigger function
// so the ability goes on cooldown properly
// 4. (If the ability doesn't require an entity go to step 6.)
// Create a new Entity Type, Entity Renderer, and register them, the type in common code, the renderer on the client only
// 5. Implement the new Entity Type, and the Renderer, the renderer can be mostly left blank. See CleaveEntityRenderer
// 6. I think that's it, add new steps in the future if more steps are required
public abstract class PlayerClassAbility<T extends PlayerClassAbility<?>> {
    protected long minLevel;
    protected long nextTriggerTime; //In ticks

    protected AttachedAbility ability;

    public PlayerClassAbility(AttachedAbility ability) {
        this.nextTriggerTime = ability.getNtt();
        this.ability = ability;
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
