package com.avaris.avarisgates.core.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public record SocketEffect(int type, long value) {

    public void apply(LivingEntity entity){
        
    }

    public SocketEffectType getType(){
        return SocketEffectType.fromInt(this.type);
    }
    public Text getTooltipText() {
        return this.getModifierTooltipText().append(this.getType().getTooltipText());
    }

    private MutableText getModifierTooltipText() {
        if(!this.getType().hasModifier){
            return Text.empty();
        }

        String s = "";
        if(this.value > 0){
            s += "+";
        }
        s += value;
        if(this.getType().isPercentValue){
            s += "%";
        }
        s += " ";
        return Text.literal(s);
    }

    public static void appendTooltip(List<Text> tooltip, Collection<SocketEffect> effects){
        boolean isExtendedDesc = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT);

        if(!isExtendedDesc){
            effects = SocketEffect.combineEffects(effects);
        }
        for(SocketEffect effect : effects){
            tooltip.add(effect.getTooltipText());
        }
    }


    public enum SocketEffectType{
        AddIntelligence(true,false),
        AddVitality(true,false);

        private final boolean isPercentValue;
        private final boolean hasModifier;

        SocketEffectType(boolean hasModifier,boolean isPercentValue){
            this.hasModifier = hasModifier;
            this.isPercentValue = isPercentValue;
        }

        public static SocketEffectType fromInt(int i){
            try {
                return SocketEffectType.values()[i];
            }catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
        }

        public Text getTooltipText() {
            switch (this){
                case AddIntelligence -> {
                    return Text.literal("Intelligence").formatted(Formatting.BLUE);
                }
                case AddVitality -> {
                    return Text.literal("Vitality").formatted(Formatting.RED);
                }
            }
            return Text.empty();
        }

    }

    public static Collection<SocketEffect> combineEffects(Collection<SocketEffect> effects){
        HashMap<SocketEffectType,Long> map = new HashMap<>();
        for (SocketEffect effect : effects){
            // TODO: account for multiplicative values
            map.compute(effect.getType(),(k,v) -> (v == null) ? effect.value : effect.value + v);
        }
        return map.entrySet()
                .stream()
                .map((e) -> new SocketEffect(e.getKey().ordinal(),e.getValue()))
                .collect(Collectors.toList());
    }

    public static final Codec<SocketEffect> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.INT.fieldOf("type").forGetter(SocketEffect::type),
                Codec.LONG.fieldOf("value").forGetter(SocketEffect::value)
        ).apply(builder, SocketEffect::new));

    public static final Codec<List<SocketEffect>> LIST_CODEC = CODEC.listOf();

}
