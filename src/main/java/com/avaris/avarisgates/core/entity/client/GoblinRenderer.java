package com.avaris.avarisgates.core.entity.client;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.entity.custom.GoblinEntity;
import com.avaris.avarisgates.core.entity.state.GoblinRenderState;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class GoblinRenderer extends MobRenderer<GoblinEntity, GoblinRenderState, GoblinModel> {
    public static final Identifier TEXTURE_LOCATION = AvarisGates.id("textures/entity/goblin.png");

    public GoblinRenderer(EntityRendererFactory.Context context) {
        super(context, new GoblinModel<>(context.getPart(GoblinModel.GOBLIN)), 0.75f);
    }

    @Override
    public Vec3 getRenderOffset(GoblinRenderState renderState) {
        return new Vec3(0.0, -0.0975, 0.0);
    }

    @Override
    public void extractRenderState(GoblinEntity entity, GoblinRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        // Add any specific state extraction logic here
    }

    @Override
    public GoblinRenderState createRenderState() {
        return new GoblinRenderState();
    }

    @Override
    public Identifier getTextureLocation(GoblinRenderState renderState) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected void setupRotations(GoblinRenderState renderState, MatrixStack matrixStack, float bodyRot, float scale) {
        super.setupRotations(renderState, matrixStack, bodyRot, scale);
    }

    @Override
    protected float getFlipDegrees() {
        return 80.0F;
    }

    @Override
    protected void scale(GoblinRenderState renderState, MatrixStack matrixStack) {
        if (renderState.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }
    }

    @Override
    protected AABB getBoundingBoxForCulling(GoblinEntity goblinEntity) {
        return super.getBoundingBoxForCulling(goblinEntity).inflate(1.0);
    }
}