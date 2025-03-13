package com.avaris.avarisgates.core;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.item.SocketEffect;
import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import java.util.List;

public class ModComponents {
    public static final ComponentType<Integer> FREE_SOCKETS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            AvarisGates.id("free_sockets"),
            ComponentType.<Integer>builder()
                .packetCodec(PacketCodecs.INTEGER)
                .codec(Codec.INT)
                .cache()
                .build()
    );
    public static final ComponentType<List<SocketEffect>> SOCKET_EFFECTS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            AvarisGates.id("item_socket_effects"),
            ComponentType.<List<SocketEffect>>builder()
                    .codec(SocketEffect.LIST_CODEC)
                    .cache()
                    .build()
    );

    public static void init(){
        AvarisGates.LOGGER.info("Initializing Components");
    }
}
