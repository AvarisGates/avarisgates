package com.avaris.avarisgates.core.api.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class ClientPlayerEvents {

    public static final Event<PlayerDoAttackMiss> PLAYER_DO_ATTACK_MISS_EVENT = EventFactory.createArrayBacked(PlayerDoAttackMiss.class,(callbacks) -> (player, stack) ->{
        for(var callback : callbacks){
            callback.onPlayerDoAttackMiss(player,stack);
        }
    });

    @FunctionalInterface
    public interface PlayerDoAttackMiss{
        void onPlayerDoAttackMiss(ClientPlayerEntity player, ItemStack stack);
    }

}
