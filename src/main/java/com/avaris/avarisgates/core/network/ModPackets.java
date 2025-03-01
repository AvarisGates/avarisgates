package com.avaris.avarisgates.core.network;

import com.avaris.avarisgates.core.player.PlayerManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModPackets {
    public static void init(){

        PayloadTypeRegistry.playC2S().register(CastPlayerClassAbilityC2S.ID, CastPlayerClassAbilityC2S.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CastPlayerClassAbilityC2S.ID, PlayerManager::receiveAbilityPacket);

        PayloadTypeRegistry.playS2C().register(ChangeAbilityS2C.ID, ChangeAbilityS2C.CODEC);

        PayloadTypeRegistry.playC2S().register(RequestAttributeIncrementC2S.ID, RequestAttributeIncrementC2S.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RequestAttributeIncrementC2S.ID, PlayerManager::receiveAttributeIncrement);

        PayloadTypeRegistry.playS2C().register(SyncAttributeS2C.ID, SyncAttributeS2C.CODEC);
    }
}
