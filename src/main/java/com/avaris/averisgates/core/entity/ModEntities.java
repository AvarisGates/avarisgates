package com.avaris.averisgates.core.entity;

import com.avaris.averisgates.Averisgates;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final Identifier CLEAVE_ID = Averisgates.id("cleave");
    public static final Identifier WHIRLWIND_ID = Averisgates.id("whirlwind");

    public static final RegistryKey<EntityType<?>> CLEAVE_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE,CLEAVE_ID);
    public static final RegistryKey<EntityType<?>> WHIRLWIND_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE,WHIRLWIND_ID);

    public static final EntityType<CleaveEntity> CLEAVE = Registry.register(
            Registries.ENTITY_TYPE,
            Averisgates.id("cleave"),
            EntityType.Builder
                    .create(CleaveEntity::new, SpawnGroup.MISC)
                    .dimensions(1.75f, 2)
                    .build(CLEAVE_KEY)
    );

    public static final EntityType<WhirlwindEntity> WHIRLWIND = Registry.register(
            Registries.ENTITY_TYPE,
            Averisgates.id("whirlwind"),
            EntityType.Builder
                    .create(WhirlwindEntity::new, SpawnGroup.MISC)
                    .dimensions(1.75f, 2)
                    .build(WHIRLWIND_KEY)
    );

    private static <T extends Entity> net.minecraft.entity.EntityType<T> register(RegistryKey<net.minecraft.entity.EntityType<?>> key, net.minecraft.entity.EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static RegistryKey<net.minecraft.entity.EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, Averisgates.id(id));
    }

    private static <T extends Entity> net.minecraft.entity.EntityType<T> register(String id, net.minecraft.entity.EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static void init(){

    }

    @Environment(EnvType.CLIENT)
    public static void initClient(){
    }
}
