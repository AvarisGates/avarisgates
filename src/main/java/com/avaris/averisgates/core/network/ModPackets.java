package com.avaris.averisgates.core.network;

import com.avaris.averisgates.core.PlayerManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPackets {
    public static void init(){

        PayloadTypeRegistry.playC2S().register(CastPlayerClassAbilityC2S.ID, CastPlayerClassAbilityC2S.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CastPlayerClassAbilityC2S.ID, PlayerManager::receiveAbilityPacket);

    }
}
