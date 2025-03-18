package com.avaris.avarisgates.common.entity.ability.model;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.EntityRenderState;

// Made with Blockbench 4.12.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports
public class MagicOrbModel extends EntityModel<EntityRenderState> {
	private final ModelPart bb_main;
	public MagicOrbModel(ModelPart root) {
        super(root);
        this.bb_main = root.getChild("orb");
	}
	public static TexturedModelData getTexturedModelData(){
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData orb = modelPartData.addChild("orb", ModelPartBuilder.create().uv(18, 3).cuboid(1.0938F, 2.0625F, -2.125F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(12, 6).cuboid(1.0938F, 4.0625F, -1.125F, 1.0F, 1.0F, 2.0F, new Dilation(0.0F))
				.uv(6, 8).cuboid(1.0938F, 1.0625F, -1.125F, 1.0F, 3.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-0.9063F, 1.0625F, -2.125F, 2.0F, 4.0F, 4.0F, new Dilation(0.0F))
				.uv(0, 8).cuboid(-1.9063F, 1.0625F, -1.125F, 1.0F, 4.0F, 2.0F, new Dilation(0.0F))
				.uv(4, 18).cuboid(-1.9063F, 2.0625F, 0.875F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(18, 9).cuboid(-1.9063F, 2.0625F, 1.875F, 1.0F, 2.0F, 0.0F, new Dilation(0.0F))
				.uv(6, 16).cuboid(-1.9063F, 1.0625F, -1.125F, 1.0F, 0.0F, 2.0F, new Dilation(0.0F))
				.uv(0, 14).cuboid(-0.9063F, 2.0625F, 1.875F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(12, 9).cuboid(-0.9063F, 2.0625F, -3.125F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(2, 1).cuboid(1.0938F, 2.0625F, 0.875F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(2, 1).cuboid(-1.9063F, 2.0625F, -2.125F, 1.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(0, 0).cuboid(-0.9063F, 0.0625F, -1.125F, 2.0F, 1.0F, 2.0F, new Dilation(0.0F)), ModelTransform.NONE);

		ModelPartData cube_r1 = orb.addChild("cube_r1", ModelPartBuilder.create().uv(6, 13).cuboid(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0937F, 3.0625F, -0.125F, 1.5708F, 0.0F, 3.1416F));

		ModelPartData cube_r2 = orb.addChild("cube_r2", ModelPartBuilder.create().uv(12, 12).cuboid(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F))
				.uv(12, 15).cuboid(-1.0F, -1.0F, 2.0F, 2.0F, 2.0F, 1.0F, new Dilation(0.0F)), ModelTransform.of(0.0937F, 3.0625F, -0.125F, 0.0F, -1.5708F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
}