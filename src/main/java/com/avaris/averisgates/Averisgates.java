package com.avaris.averisgates;

import com.avaris.averisgates.core.*;
import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.averisgates.core.network.ModPackets;
import com.avaris.averisgates.mixin.ClampedEntityAttributeAccessor;
import net.fabricmc.api.ModInitializer;
import com.google.common.collect.ImmutableMap;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Averisgates implements ModInitializer {

    public static String MOD_ID = "averisgates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String id){
        return Identifier.of(MOD_ID,id);
    }


    @Override
    public void onInitialize() {
        AttributeFix.init();
        ModPackets.init();
    }


}

