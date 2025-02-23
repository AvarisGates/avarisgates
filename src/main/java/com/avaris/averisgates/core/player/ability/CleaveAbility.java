package com.avaris.averisgates.core.player.ability;

import com.avaris.averisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class CleaveAbility extends PlayerClassAbility {


    public CleaveAbility(Long ntt, AttachmentType<Long> slot) {
        super(ntt,slot);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.Cleave;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.Warrior;
    }

    // In minecraft ticks, 1 ticks is 1/20 of a second
    @Override
    public long getBaseCooldown() {
        return 20;
    }

    @Override
    public void trigger(MinecraftServer server, ServerPlayerEntity player) {
        super.trigger(server,player);
    }
}
