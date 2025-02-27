package com.avaris.avarisgates.core.entity.ability.renderer;

import com.avaris.avarisgates.core.entity.ability.CleaveEntity;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.EntityRenderState;

public class CleaveEntityRenderer extends EntityRenderer<CleaveEntity, CleaveEntityRenderer.CleaveEntityRenderState> {
    public CleaveEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public CleaveEntityRenderState createRenderState() {
        return new CleaveEntityRenderState();
    }

    public static class CleaveEntityRenderState extends EntityRenderState{

    }
}
