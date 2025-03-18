package com.avaris.avarisgates.core;

import com.avaris.avarisgates.core.config.ModConfig;
import com.avaris.avarisgates.mixin.ClampedEntityAttributeAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

// This class fixes the vanilla attribute cap, it isn't related to modded abilities
public class AttributeFix {
    private static final Double AttrLimit = 1000000000D; //Very important limit (VIL)
    private static final Logger LOGGER = LoggerFactory.getLogger("AvarisGates|AttributeFix");

    private static final ImmutableMap<Identifier,Double> NEW_DEFAULT_ATTRIBUTES = ImmutableMap.of(
            Identifier.ofVanilla("max_health"), AttrLimit,
            Identifier.ofVanilla("armor"), AttrLimit,
            Identifier.ofVanilla("armor_toughness"), AttrLimit,
            Identifier.ofVanilla("attack_damage"), AttrLimit,
            Identifier.ofVanilla("attack_knockback"),AttrLimit
    );

    public static void init() {
        for (Identifier id : Registries.ATTRIBUTE.getIds()) {
            Double new_def_value = NEW_DEFAULT_ATTRIBUTES.get(id);
            ClampedEntityAttributeAccessor attr = (ClampedEntityAttributeAccessor)Registries.ATTRIBUTE.get(id);
            attr.attributefix$setMaxValue(Objects.requireNonNullElse(new_def_value, AttrLimit));
            if(ModConfig.PRINT_ATTRIBUTE_FIX_INFO.getValue()){
                LOGGER.info("Set {} max value to: {}",id,new_def_value == null ? AttrLimit : new_def_value);
            }
        }
    }
}
