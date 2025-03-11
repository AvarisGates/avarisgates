package com.avaris.avarisgates.core.entity.ability.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.animation.Animation;
import net.minecraft.client.render.entity.animation.AnimationHelper;
import net.minecraft.client.render.entity.animation.Keyframe;
import net.minecraft.client.render.entity.animation.Transformation;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;


public class FireBoltModel extends EntityModel<EntityRenderState> {
    private final ModelPart firebolt;
    public FireBoltModel(ModelPart root) {
        super(root);
        this.firebolt = root.getChild("firebolt");
    }
    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData firebolt = modelPartData.addChild("firebolt", ModelPartBuilder.create().uv(16, 0).cuboid(1.0F, -1.0F, -4.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(16, 7).cuboid(-2.0F, -1.0F, -4.0F, 1.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 0).cuboid(-1.0F, -1.0F, -5.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 7).cuboid(-1.0F, 0.0F, -4.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F))
                .uv(0, 14).cuboid(-1.0F, -2.0F, -4.0F, 2.0F, 1.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 23.0F, -3.0F));
        return TexturedModelData.of(modelData, 32, 32);
    }

   // @Override
   // public void setAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
   // }
   // @Override
   // public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
   //     firebolt.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
   // }
    public static final Animation shooting = Animation.Builder.create(0.625F).looping()
            .addBoneAnimation("firebolt", new Transformation(Transformation.Targets.ROTATE,
                    new Keyframe(0.0F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 0.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.0417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 25.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.0833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 50.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.125F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 75.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.1667F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 100.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.2083F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 125.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.25F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 150.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.2917F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 175.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.3333F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 200.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.375F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 225.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.4167F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 250.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.4583F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 275.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 300.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5417F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 325.0F), Transformation.Interpolations.LINEAR),
                    new Keyframe(0.5833F, AnimationHelper.createRotationalVector(0.0F, 0.0F, 350.0F), Transformation.Interpolations.LINEAR)
            ))
            .build();

}
