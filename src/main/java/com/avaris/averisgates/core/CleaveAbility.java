package com.avaris.averisgates.core;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class CleaveAbility extends PlayerClassAbility{

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
    }
}
