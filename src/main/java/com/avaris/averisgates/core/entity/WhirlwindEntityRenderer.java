package com.avaris.averisgates.core.entity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class WhirlwindEntityRenderer extends EntityRenderer<WhirlwindEntity, WhirlwindEntityRenderer.WhirlwindEntityRenderState> {
    public WhirlwindEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public WhirlwindEntityRenderState createRenderState() {
        return new WhirlwindEntityRenderState();
    }

    public static class WhirlwindEntityRenderState extends EntityRenderState{

    }
}
