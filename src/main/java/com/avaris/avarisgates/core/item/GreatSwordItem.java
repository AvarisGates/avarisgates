package com.avaris.avarisgates.core.item;

import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

public class GreatSwordItem extends SwordItem {
    public static final Settings SETTINGS = new Settings().registryKey(ModItems.GREAT_SWORD_KEY);

    public GreatSwordItem(Settings settings) {
        super(ToolMaterial.NETHERITE, 9, 1, settings);
    }
}
