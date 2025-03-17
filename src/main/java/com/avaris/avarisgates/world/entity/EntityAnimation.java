package com.avaris.avarisgates.world.entity;

import com.avaris.avarisgates.AvarisGates;
import net.minecraft.resources.ResourceLocation;

public record EntityAnimation(ResourceLocation resourceLocation, int duration) {
    public static final EntityAnimation NONE = new EntityAnimation("none", 0);

    public EntityAnimation(String path, int duration){
        this(AvarisGates.id(path), duration);
    }
}
