package com.avaris.avarisgates.common.player.attribute;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.common.item.SocketableItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

/**
 * An attribute instance attached to a living entity
 */
public class Attribute {
    private final AttributeType type;
    private final long value;

    /**
     * Init a new instance storing the values
     **/
    public Attribute(AttributeType type, long value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Get an attribute of a given type attached to a LivingEntity (An entity with health and such)
     * This function doesn't take into account SocketEffects
     * If the attachments aren't found attach default values
     * @param entity the entity to query
     * @param type the attribute type to query
     * @return The attached attribute instance or a new attribute instance with default value. Never null.
     */
    public static @NotNull Attribute getAttributeValue(LivingEntity entity, AttributeType type){
        Long attached = entity.getAttachedOrCreate(type.toValueAttachment());
        if(attached == null){
            return resetAttribute(entity,type);
        }

        return new Attribute(type,entity.getAttachedOrCreate(type.toValueAttachment()));
    }

    /**
     * Get an attribute of a given type attached to a LivingEntity (An entity with health and such)
     * This function takes into account SocketEffects
     * If the attachments aren't found attach default values
     **/
    public static @NotNull Attribute getAttributeWithEffects(LivingEntity entity, AttributeType type){
        Attribute attribute = getAttributeValue(entity,type);
        long modifier = SocketableItem.getAttributeAdditiveModifications(entity,type);
        //TODO: add multiplicative modifiers
        //long modifier = SocketableItem.getAttributeAdditiveModifications(entity,type);
        return new Attribute(type,attribute.value + modifier);
    }

    /**
     * Initialize attachment values for a player if it's not present
     * Then sync it to the client
     * Should be called when a player joins a world
     * @param player the player to be initialized
     */
    public static void initForPlayer(ServerPlayerEntity player) {
        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttributeWithEffects(player,type);
            attribute.apply(player);
            AvarisGates.LOGGER.info("{}",attribute);
        }
    }

    /**
     * Apply attribute values to vanilla attributes.
     * Called after{@link Attribute#setAttribute}
     * @param entity the entity to be updated
     */
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
        // TODO: Add conversions for Strength and Dex

        if(type == AttributeType.Strength){
            Identifier attackDamage = Identifier.ofVanilla("attack_damage");
            EntityAttribute attr = Registries.ATTRIBUTE.get(attackDamage);
            entity.getAttributeInstance(Registries.ATTRIBUTE.getEntry(attr)).setBaseValue(Attribute.getAttributeWithEffects(entity,AttributeType.Strength).getValue());
        }
        if(type == AttributeType.Dexterity){
            Identifier attackSpeed = Identifier.ofVanilla("attack_speed");
            EntityAttribute attr = Registries.ATTRIBUTE.get(attackSpeed);
            entity.getAttributeInstance(Registries.ATTRIBUTE.getEntry(attr)).setBaseValue(Attribute.getAttributeWithEffects(entity,AttributeType.Dexterity).getValue());
        }
    }

    /**
     * Sets attribute value and applies it to vanilla attributes
     * @param entity the entity to be updated
     * @param attribute the attribute instance
     * @return current attribute, which should have the same values as the passed attribute
     **/
    public static Attribute setAttribute(LivingEntity entity,Attribute attribute){
        entity.setAttached(attribute.type.toValueAttachment(), attribute.value);
        attribute.apply(entity);
        return attribute;
    }

    /**
     * @see Attribute#setAttribute(LivingEntity,Attribute)
     */
    public static Attribute setAttribute(LivingEntity entity,AttributeType type,long value){
        Attribute attr = new Attribute(type,value);
        return setAttribute(entity,attr);
    }

    /**
     * Resets attribute value to the default one and applies it to vanilla attributes
     * @see Attribute#setAttribute(LivingEntity, Attribute)
     **/
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

    /**
     * Retrieves the underlying attribute value
     * @return the value as a long
     */
    public long getValue() {
       return value;
    }

    /**
     * Retrieves the attribute instance type
     * @return the type
     */
    public AttributeType getType() {
        return type;
    }

    /**
     * A helper function used to apply(update) all attributes to a player
     * @param player the player to be updated
     */
    public static void updateAttributes(ServerPlayerEntity player) {
        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttributeWithEffects(player,type);
            attribute.apply(player);
        }
    }
}
