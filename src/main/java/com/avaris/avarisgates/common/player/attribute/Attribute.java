package com.avaris.avarisgates.common.player.attribute;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.common.item.SocketableItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

// An attribute instance attached to a living entity
public class Attribute {
    private final AttributeType type;
    private final long value;

    // Init a new instance storing the values
    public Attribute(AttributeType type, long value) {
        this.type = type;
        this.value = value;
    }

    // Get an attribute of a given type attached to a LivingEntity (An entity with health and such)
    // This function doesn't take into account SocketEffects
    // If the attachments aren't found attach default values
    public static Attribute getAttributeValue(LivingEntity entity, AttributeType type){
        Long attached = entity.getAttachedOrCreate(type.toValueAttachment());
        if(attached == null){
            return resetAttribute(entity,type);
        }

        return new Attribute(type,entity.getAttachedOrCreate(type.toValueAttachment()));
    }

    // Get an attribute of a given type attached to a LivingEntity (An entity with health and such)
    // This function takes into account SocketEffects
    // If the attachments aren't found attach default values
    public static Attribute getAttributeWithEffects(LivingEntity entity, AttributeType type){
        Attribute attribute = getAttributeValue(entity,type);
        long modifier = SocketableItem.getAttributeAdditiveModifications(entity,type);
        //TODO: add multiplicative modifiers
        //long modifier = SocketableItem.getAttributeAdditiveModifications(entity,type);
        return new Attribute(type,attribute.value + modifier);
    }

    // Initialize attachment values for a player if it's not present
    // Then sync it to the client
    // Should be called when a player joins a world
    public static void initForPlayer(ServerPlayerEntity player) {
        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttributeWithEffects(player,type);
            attribute.apply(player);
            AvarisGates.LOGGER.info("{}",attribute);
        }
    }

    // Apply attribute values to vanilla attributes
    // Called after Attribute.setAttribute
    private void apply(LivingEntity entity) {
        if(type == AttributeType.Vigor){
            Identifier health_id = Identifier.ofVanilla("max_health");
            EntityAttribute attr = Registries.ATTRIBUTE.get(health_id);
            entity.getAttributeInstance(Registries.ATTRIBUTE.getEntry(attr))
                    .setBaseValue(2 * Attribute.getAttributeWithEffects(entity,AttributeType.Vitality).getValue());
        }
        if(type == AttributeType.Agility){
            Identifier movement_speed = Identifier.ofVanilla("movement_speed");
            EntityAttribute attr = Registries.ATTRIBUTE.get(movement_speed);
            Attribute.getAttributeWithEffects(entity,AttributeType.Vitality).getValue();
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

    // Sets attribute value and applies it to vanilla attributes
    public static Attribute setAttribute(LivingEntity entity,Attribute attribute){
        entity.setAttached(attribute.type.toValueAttachment(), attribute.value);
        attribute.apply(entity);
        return attribute;
    }

    // Sets attribute value and applies it to vanilla attributes
    public static Attribute setAttribute(LivingEntity entity,AttributeType type,long value){
        Attribute attr = new Attribute(type,value);
        return setAttribute(entity,attr);
    }

    // Resets attribute value to the default one and applies it to vanilla attributes
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

    public static void updateAttributes(ServerPlayerEntity player) {
        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttributeWithEffects(player,type);
            attribute.apply(player);
        }
    }
}
