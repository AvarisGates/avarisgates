package com.avaris.avarisgates.core.player.attribute;

import com.avaris.avarisgates.AvarisGates;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

// An attribute instance attached to a living entity
public class Attribute {
    private final AttributeType type;
    private final long value;

    public Attribute(AttributeType type, long value) {
        this.type = type;
        this.value = value;
    }

    public static Attribute getAttribute(LivingEntity entity, AttributeType type){
        Long attached = entity.getAttachedOrCreate(type.toValueAttachment());
        if(attached == null){
            return resetAttribute(entity,type);
        }

        return new Attribute(type,entity.getAttachedOrCreate(type.toValueAttachment()));
    }

    public static void initForPlayer(ServerPlayerEntity player) {
        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttribute(player,type);
            AvarisGates.LOGGER.info("{}",attribute);
        }
    }

    private void apply(LivingEntity entity) {
        if(type == AttributeType.Vigor){
            Identifier health_id = Identifier.ofVanilla("max_health");
            EntityAttribute attr = Registries.ATTRIBUTE.get(health_id);
            entity.getAttributeInstance(Registries.ATTRIBUTE.getEntry(attr))
                    .setBaseValue(2 * this.getValue());
        }
        if(type == AttributeType.Agility){
            Identifier movement_speed = Identifier.ofVanilla("movement_speed");
            EntityAttribute attr = Registries.ATTRIBUTE.get(movement_speed);
            Attribute.getAttribute(entity,AttributeType.Vitality).getValue();
            double newValue = 1;
            if(this.value <= 110){
                newValue = 1 + (this.value - 10) * 0.01;
            }else{
                newValue = 1 + 100 * 0.01 + (this.value - 110) * 0.001;
            }

            // Convert the value to be compatible with vanilla minecraft attributes
            entity.getAttributeInstance(Registries.ATTRIBUTE.getEntry(attr)).setBaseValue(newValue * 0.09);
        }
    }

    public static Attribute setAttribute(LivingEntity entity,Attribute attribute){
        entity.setAttached(attribute.type.toValueAttachment(), attribute.value);
        attribute.apply(entity);
        return attribute;
    }

    public static Attribute setAttribute(LivingEntity entity,AttributeType type,long value){
        Attribute attr = new Attribute(type,value);
        setAttribute(entity,attr);
        return attr;
    }

    public static Attribute resetAttribute(LivingEntity entity,AttributeType type){
        return setAttribute(entity,type, type.defaultValue());
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "type=" + type +
                ", value=" + value +
                '}';
    }

    public long getValue() {
       return value;
    }

    public AttributeType getType() {
        return type;
    }
}
