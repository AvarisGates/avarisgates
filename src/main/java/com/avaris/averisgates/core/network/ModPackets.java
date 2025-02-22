package com.avaris.averisgates.core.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class ModPackets {
    public static void init(){
        PayloadTypeRegistry.playC2S().register(CastPlayerClassAbilityC2S.ID, CastPlayerClassAbilityC2S.CODEC);
    }
}
