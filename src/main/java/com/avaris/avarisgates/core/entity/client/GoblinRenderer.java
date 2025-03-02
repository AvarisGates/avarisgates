package com.avaris.avarisgates.core.entity.client;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.entity.custom.GoblinEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

//TODO: FIX THIS, GOTTA ADD THE STATE THING INSTEAD OF "GoblinState" and fix the rest too, help
public class GoblinRenderer extends MobEntityRenderer<GoblinEntity,GoblinState, GoblinModel<GoblinEntity>> {
    public GoblinRenderer(EntityRendererFactory.Context context){
        super(context, new GoblinModel<>(context.getPart(GoblinModel.GOBLIN)), 0.75f); //0.75 is the shadow
    }

    @Override
    public Identifier getTexture(GoblinEntity entity){
        return Identifier.of(AvarisGates.MOD_ID, "textures/entity/goblin/goblin.png");
    }

    @Override
    public void render(GoblinEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i){
        if(livingEntity.isBaby()){
            matrixStack.scale(0.5f,0.5f,0.5f); //BABY GOBLINO
        } else {
            matrixStack.scale(1f,1f,1f); //NORMAL GOBLINO
        }
        super.render(livingEntity, f, g matrixStack, vertexConsumerProvider, i);
    }
}
