package com.avaris.avarisgates.core.entity.ability.renderer;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.entity.ability.FireBoltEntity;
import com.avaris.avarisgates.core.entity.ability.model.FireBoltModel;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.state.TridentEntityRenderState;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

public class FireBoltEntityRenderer extends EntityRenderer<FireBoltEntity, TridentEntityRenderState> {
    public static final Identifier TEXTURE = AvarisGates.id("textures/entity/firebolt.png");
    private final FireBoltModel model;
    public FireBoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new FireBoltModel(FireBoltModel.getTexturedModelData().createModel());
    }

    public void render(TridentEntityRenderState tridentEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(tridentEntityRenderState.yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(tridentEntityRenderState.pitch));
        //matrixStack.scale(10,10,10);
        VertexConsumer vertexConsumer = ItemRenderer.getItemGlintConsumer(
                vertexConsumerProvider, this.model.getLayer(TEXTURE), false, tridentEntityRenderState.enchanted
        );
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV);
        matrixStack.pop();
        super.render(tridentEntityRenderState, matrixStack, vertexConsumerProvider, i);
    }

    public TridentEntityRenderState createRenderState() {
        return new TridentEntityRenderState();
    }

    public void updateRenderState(FireBoltEntity tridentEntity, TridentEntityRenderState tridentEntityRenderState, float f) {
        super.updateRenderState(tridentEntity, tridentEntityRenderState, f);
        tridentEntityRenderState.yaw = tridentEntity.getLerpedYaw(f);
        tridentEntityRenderState.pitch = tridentEntity.getLerpedPitch(f);
        tridentEntityRenderState.enchanted = false;
    }
}
