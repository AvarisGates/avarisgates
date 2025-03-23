package com.avaris.avarisgates.common.compat.modshield;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.modshield.api.v1.impl.ModShieldEventApi;
import net.fabricmc.loader.api.FabricLoader;

public class ModShieldCompat {
    public static void init(){
        if(FabricLoader.getInstance().isModLoaded("modshield")){
            ModShieldEventApi.PLAYER_ALLOWED_EVENT.register((uuid, map)->{
                AvarisGates.LOGGER.info("{} allowed :3",uuid);
            });
            ModShieldEventApi.PLAYER_DISALLOWED_EVENT.register((uuid,reason) -> {
                AvarisGates.LOGGER.info("{} disallowed for: {}",uuid,reason);
            });
        }
    }
}
