package com.avaris.avarisgates.common.item;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class FuckYouGemItem extends Item implements InsertableItem {
    public FuckYouGemItem(Settings settings) {
        super(settings);
    }

    @Override
    public Collection<SocketEffect> getEffects(ItemStack stack) {
        return List.of(
                    new SocketEffect(SocketEffect.SocketEffectType.AddAgility.ordinal(),-100),
                    new SocketEffect(SocketEffect.SocketEffectType.AddIntelligence.ordinal(),-100),
                    new SocketEffect(SocketEffect.SocketEffectType.AddVitality.ordinal(),-100)
                );
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
        this.appendEffectsTooltip(stack, context, textConsumer, type);
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        return super.getTooltipData(stack);
    }
}
