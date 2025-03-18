package com.avaris.avarisgates.common.entity;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.common.entity.ability.CleaveEntity;
import com.avaris.avarisgates.common.entity.ability.FireBoltEntity;
import com.avaris.avarisgates.common.entity.ability.MagicOrbEntity;
import com.avaris.avarisgates.common.entity.ability.WhirlwindEntity;
import com.avaris.avarisgates.common.entity.ability.renderer.CleaveEntityRenderer;
import com.avaris.avarisgates.common.entity.ability.renderer.FireBoltEntityRenderer;
import com.avaris.avarisgates.common.entity.ability.renderer.MagicOrbEntityRenderer;
import com.avaris.avarisgates.common.entity.ability.renderer.WhirlwindEntityRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final Identifier CLEAVE_ID = AvarisGates.id("cleave");
    public static final Identifier WHIRLWIND_ID = AvarisGates.id("whirlwind");
    public static final Identifier FIREBOLT_ID = AvarisGates.id("fireball");
    public static final Identifier MAGICORB_ID = AvarisGates.id("magicorb");

    public static final RegistryKey<EntityType<?>> CLEAVE_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE,CLEAVE_ID);
    public static final RegistryKey<EntityType<?>> WHIRLWIND_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE,WHIRLWIND_ID);

    public static final RegistryKey<EntityType<?>> FIREBOLT_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, FIREBOLT_ID);
    public static final RegistryKey<EntityType<?>> MAGICORB_KEY = RegistryKey.of(RegistryKeys.ENTITY_TYPE, MAGICORB_ID);

    public static final EntityType<CleaveEntity> CLEAVE = Registry.register(
            Registries.ENTITY_TYPE,
            CLEAVE_ID,
            EntityType.Builder
                    .create(CleaveEntity::new, SpawnGroup.MISC)
                    .dimensions(1.75f, 2)
                    .build(CLEAVE_KEY)
    );

    public static final EntityType<WhirlwindEntity> WHIRLWIND = Registry.register(
            Registries.ENTITY_TYPE,
            WHIRLWIND_ID,
            EntityType.Builder
                    .create(WhirlwindEntity::new, SpawnGroup.MISC)
                    .dimensions(1.75f, 2)
                    .build(WHIRLWIND_KEY)
    );

    public static final EntityType<FireBoltEntity> FIREBOLT = Registry.register(
            Registries.ENTITY_TYPE,
            FIREBOLT_ID,
            EntityType.Builder
                    .create(FireBoltEntity::new, SpawnGroup.MISC)
                    .dimensions(2, 2)
                    .build(FIREBOLT_KEY)
    );

    public static final EntityType<MagicOrbEntity> MAGICORB = Registry.register(
            Registries.ENTITY_TYPE,
            MAGICORB_ID,
            EntityType.Builder
                    .create(MagicOrbEntity::new, SpawnGroup.MISC)
                    .dimensions(0.2f, 0.2f)
                    .build(MAGICORB_KEY)
    );

    private static <T extends Entity> net.minecraft.entity.EntityType<T> register(RegistryKey<net.minecraft.entity.EntityType<?>> key, net.minecraft.entity.EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static RegistryKey<net.minecraft.entity.EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, AvarisGates.id(id));
    }

    private static <T extends Entity> net.minecraft.entity.EntityType<T> register(String id, net.minecraft.entity.EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    public static void init(){

    }

    @Environment(EnvType.CLIENT)
    public static void registerEntityRenderers(){
        EntityRendererRegistry.register(ModEntities.CLEAVE, CleaveEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.WHIRLWIND, WhirlwindEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIREBOLT, FireBoltEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.MAGICORB, MagicOrbEntityRenderer::new);
    }
}
