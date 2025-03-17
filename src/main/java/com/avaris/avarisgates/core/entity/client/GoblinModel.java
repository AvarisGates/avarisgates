package com.avaris.avarisgates.core.entity.client;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.entity.custom.GoblinEntity;
import com.avaris.avarisgates.core.entity.state.GoblinRenderState;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.Optional;

public class GoblinModel extends EntityModel<GoblinRenderState> {
    public static final EntityModelLayer GOBLIN = new EntityModelLayer(Identifier.of(AvarisGates.MOD_ID, "goblin"), "main");

    private final ModelPart goblin;
    private final ModelPart head;
    private final ModelPart jaw;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;
    public GoblinModel(ModelPart root) {
        super(root);
        this.goblin = root.getChild("goblin");
        this.head = this.goblin.getChild("head");
        this.jaw = this.head.getChild("jaw");
        this.body = this.goblin.getChild("body");
        this.rightArm = this.goblin.getChild("rightArm");
        this.leftArm = this.goblin.getChild("leftArm");
        this.rightLeg = this.goblin.getChild("rightLeg");
        this.leftLeg = this.goblin.getChild("leftLeg");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData goblin = modelPartData.addChild("goblin", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = goblin.addChild("head", ModelPartBuilder.create().uv(0, 1).cuboid(-4.0F, -5.0F, -4.0F, 8.0F, 5.0F, 8.0F, new Dilation(0.0F))
                .uv(32, 0).cuboid(-4.0F, -6.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.5F)), ModelTransform.of(0.0F, -22.0F, 0.0F, -0.1047F, 0.0873F, 0.0F));

        ModelPartData jaw = head.addChild("jaw", ModelPartBuilder.create().uv(0, 0).cuboid(-4.0F, 0.0F, -5.0F, 8.0F, 2.0F, 8.0F, new Dilation(0.0F))
                .uv(2, 1).cuboid(-3.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F))
                .uv(2, 1).cuboid(2.0F, -1.0F, -5.0F, 1.0F, 1.0F, 1.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartData body = goblin.addChild("body", ModelPartBuilder.create().uv(16, 16).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(16, 32).cuboid(-4.0F, 2.0F, -2.0F, 8.0F, 10.0F, 4.0F, new Dilation(0.25F)), ModelTransform.pivot(0.0F, -22.0F, 0.0F));

        ModelPartData rightArm = goblin.addChild("rightArm", ModelPartBuilder.create().uv(40, 16).cuboid(-2.0F, 0.0F, -2.0F, 3.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(40, 32).cuboid(-2.0F, 0.0F, -2.0F, 3.0F, 10.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-5.0F, -20.0F, 0.0F, -0.1745F, 0.0F, 0.0F));

        ModelPartData leftArm = goblin.addChild("leftArm", ModelPartBuilder.create().uv(32, 48).cuboid(-1.0F, 0.0F, -2.0F, 3.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(48, 48).cuboid(-1.0F, 0.0F, -2.0F, 3.0F, 10.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(5.0F, -20.0F, 0.0F, 0.2094F, 0.0F, 0.0F));

        ModelPartData rightLeg = goblin.addChild("rightLeg", ModelPartBuilder.create().uv(0, 16).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 32).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(-1.9F, -10.0F, 0.0F, 0.192F, 0.0F, 0.0349F));

        ModelPartData leftLeg = goblin.addChild("leftLeg", ModelPartBuilder.create().uv(16, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.0F))
                .uv(0, 48).cuboid(-2.0F, 0.0F, -2.0F, 4.0F, 10.0F, 4.0F, new Dilation(0.25F)), ModelTransform.of(1.9F, -10.0F, 0.0F, -0.1745F, 0.0F, -0.0349F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void setAngles(GoblinEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.getPart().traverse().forEach(ModelPart::resetTransform);
        this.setHeadAngles(netHeadYaw, headPitch);

        //TODO: TO FIX ç_ç i'm sorry future self
        //this.animateMovement(GoblinAnimations.ANIMATION_GOBLIN_IDLE, limbSwing, limbSwingAmount, 2f, 2.5f); //TODO: CHANGE THIS WITH A WALKING ANIMATION
        //this.updateAnimation(entity.idleAnimationState, GoblinAnimations.ANIMATION_GOBLIN_IDLE, ageInTicks, 1f);
    }

    private void setHeadAngles(float headYaw, float headPitch) {
        headYaw = MathHelper.clamp(headYaw, -30.0F, 30.0F);
        headPitch = MathHelper.clamp(headPitch, -25.0F, 45.0F);

        this.head.yaw = headYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;
    }


    public void renderToBuffer(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, int color) {
        goblin.render(matrices, vertexConsumer, light, overlay, color);
    }


    public ModelPart getPart() {
        return goblin;
    }
}
