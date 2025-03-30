package com.avaris.avarisgates.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

public class GreatSwordItem extends Item {
    public static final Settings SETTINGS = new Settings().registryKey(ModItems.GREAT_SWORD_KEY)
            .sword(ToolMaterial.NETHERITE,9,1);

    public GreatSwordItem(Settings settings) {
        super(settings);
    }
}
