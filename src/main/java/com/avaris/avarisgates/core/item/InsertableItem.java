package com.avaris.avarisgates.core.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public interface InsertableItem {
    Collection<SocketEffect> getEffects(ItemStack stack);

    // Remember to add this to inheritor's Item.appendTooltip implementation
    default void appendEffectsTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        SocketEffect.appendTooltip(tooltip,getEffects(stack));
    }
}
