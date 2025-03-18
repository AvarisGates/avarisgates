package com.avaris.avarisgates.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;

public class RubyGemItem extends Item implements InsertableItem {
    public RubyGemItem(Settings settings) {
        super(settings);
    }

    @Override
    public Collection<SocketEffect> getEffects(ItemStack stack) {
        return List.of(
                    new SocketEffect(SocketEffect.SocketEffectType.AddIntelligence.ordinal(),-1),
                    new SocketEffect(SocketEffect.SocketEffectType.AddVitality.ordinal(),5)
                );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        super.appendTooltip(stack, context, tooltip, type);
        this.appendEffectsTooltip(stack, context, tooltip, type);
    }
}
