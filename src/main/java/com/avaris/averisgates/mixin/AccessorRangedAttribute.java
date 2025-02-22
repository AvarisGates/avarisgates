package com.avaris.averisgates.mixin;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;


@Mixin(ClampedEntityAttribute.class)
public interface AccessorRangedAttribute {
    @Accessor("minValue")
    @Mutable
    void attributefix$setMinValue(double minValue);

    @Accessor("maxValue")
    @Mutable
    void attributefix$setMaxValue(double maxValue);
}
