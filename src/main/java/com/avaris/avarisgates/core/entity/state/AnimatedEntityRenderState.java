package com.avaris.avarisgates.core.entity.state;

import com.avaris.avarisgates.world.entity.AnimatedEntity;
import com.avaris.avarisgates.world.entity.EntityAnimation;
import net.minecraft.client.render.entity.state.ArmedEntityRenderState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.item.ItemModelResolver;

public class AnimatedEntityRenderState extends ArmedEntityRenderState {
    public float animationTime;
    public EntityAnimation animation;

    public static <T extends LivingEntity & AnimatedEntity> void extractAnimatedEntityRenderState(T animatedEntity, AnimatedEntityRenderState reusedState, float partialTick, ItemModelResolver itemModelResolver){
        ArmedEntityRenderState.extractArmedEntityRenderState(animatedEntity, reusedState, itemModelResolver);
        reusedState.animationTime =
                animatedEntity.getAnimationTick() > 0 ? animatedEntity.getAnimationTick() + partialTick :
                        animatedEntity.getAnimationTick();
        reusedState.animation = animatedEntity.getAnimation();
    }
}
