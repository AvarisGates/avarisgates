package com.avaris.avarisgates.core.dim;

import com.avaris.avarisgates.AvarisGates;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

import java.util.OptionalLong;

public class ModDimensions {

    public static final Identifier DUNGEON_DIM_ID = AvarisGates.id("dungeon_dimension");

    public static final RegistryKey<DimensionOptions> DUNGEON_DIM_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            DUNGEON_DIM_ID);


    public static final RegistryKey<World> DUNGEON_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            DUNGEON_DIM_ID);


    public static final RegistryKey<DimensionType> DUNGEON_DIM_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            DUNGEON_DIM_ID);

    public static void bootstrapType(Registerable<DimensionType> context) {
        context.register(
                DUNGEON_DIM_TYPE,
                new DimensionType(
                        OptionalLong.empty(),
                        true,
                        false,
                        false,
                        true,
                        1.0,
                        true,
                        false,
                        0,
                        64,
                        384,
                        BlockTags.INFINIBURN_OVERWORLD,
                        DUNGEON_DIM_ID,
                        0.0F,
                        new DimensionType.MonsterSettings(false, true, UniformIntProvider.create(0, 7), 0)
                )
        );
    }
}
