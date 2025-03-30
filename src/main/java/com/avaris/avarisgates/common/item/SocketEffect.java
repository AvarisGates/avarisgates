package com.avaris.avarisgates.common.item;

import com.avaris.avarisgates.common.player.attribute.AttributeType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public record SocketEffect(int type, long value) {

    public long getAttributeAdditiveModification(AttributeType type){
       if(!this.getType().isMultiplicative&&this.getType().getAttributeType() == type){
           return this.value;
       }
       return 0;
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
        if(this.getType().isMultiplicative){
            s += "%";
        }
        s += " ";
        return Text.literal(s);
    }

    public static void appendTooltip(Consumer<Text> tooltip, Collection<SocketEffect> effects){
        boolean isExtendedDesc = InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT);

        if(!isExtendedDesc){
            effects = SocketEffect.combineEffects(effects);
        }
        for(SocketEffect effect : effects){
            tooltip.accept(effect.getTooltipText());
        }
    }


    public enum SocketEffectType{
        AddStrength(true,false),
        AddVitality(true,false),
        AddVigor(true,false),
        AddDexterity(true,false),
        AddAgility(true,false),
        AddIntelligence(true,false),
        AddWill(true,false),
        AddFaith(true,false);

        private final boolean isMultiplicative;
        private final boolean hasModifier;

        SocketEffectType(boolean hasModifier,boolean isPercentValue){
            this.hasModifier = hasModifier;
            this.isMultiplicative = isPercentValue;
        }

        public static SocketEffectType fromInt(int i){
            try {
                return SocketEffectType.values()[i];
            }catch (ArrayIndexOutOfBoundsException e){
                return null;
            }
        }

        public Text getTooltipText() {
            switch (this) {
                case AddStrength -> {
                    return Text.literal("Strength").formatted(Formatting.YELLOW);
                }
                case AddVitality -> {
                    return Text.literal("Vitality").formatted(Formatting.RED);
                }
                case AddVigor -> {
                    return Text.literal("Vigor").formatted(Formatting.GREEN);
                }
                case AddDexterity -> {
                    return Text.literal("Dexterity").formatted(Formatting.AQUA);
                }
                case AddAgility -> {
                    return Text.literal("Agility").formatted(Formatting.GOLD);
                }
                case AddIntelligence -> {
                    return Text.literal("Intelligence").formatted(Formatting.BLUE);
                }
                case AddWill -> {
                    return Text.literal("Will").formatted(Formatting.DARK_PURPLE);
                }
                case AddFaith -> {
                    return Text.literal("Faith").formatted(Formatting.WHITE);
                }
            }
            return Text.empty();
        }

        public AttributeType getAttributeType() {
            switch (this){
                case AddStrength -> {
                    return AttributeType.Strength;
                }
                case AddVitality -> {
                    return AttributeType.Vitality;
                }
                case AddVigor -> {
                    return AttributeType.Vigor;
                }
                case AddDexterity -> {
                    return AttributeType.Dexterity;
                }
                case AddAgility -> {
                    return AttributeType.Agility;
                }
                case AddIntelligence -> {
                    return AttributeType.Intelligence;
                }
                case AddWill -> {
                    return AttributeType.Will;
                }
            }
            return null;
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
