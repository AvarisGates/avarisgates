package com.avaris.avarisgates.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.function.Consumer;

public interface InsertableItem {
    Collection<SocketEffect> getEffects(ItemStack stack);

    // Remember to add this to inheritor's Item.appendTooltip implementation
    default void appendEffectsTooltip(ItemStack stack, Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        SocketEffect.appendTooltip(tooltip,getEffects(stack));
    }
}
