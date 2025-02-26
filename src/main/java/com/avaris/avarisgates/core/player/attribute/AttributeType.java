package com.avaris.avarisgates.core.player.attribute;

import com.avaris.avarisgates.AvarisGates;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;

public enum AttributeType {
   Strength,
   Vitality,
   Dexterity,
   Agility,
   Intelligence,
   Will,
   Faith;

   private static AttachmentType<Long> register(String id){
      return AttachmentRegistry.create(
          AvarisGates.id(id),
          builder -> builder
                  .initializer(() -> 0L) // start with a default value like hunger
                  .persistent(Codec.LONG) // persist across restarts
                  .copyOnDeath()
                  .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
      );
   }
    public static final AttachmentType<Long> STRENGTH_ATTACHMENT = register("strength_attribute");
    public static final AttachmentType<Long> VITALITY_ATTACHMENT = register("vitality_attribute");
    public static final AttachmentType<Long> DEXTERITY_ATTACHMENT = register("dexterity_attribute");
    public static final AttachmentType<Long> AGILITY_ATTACHMENT = register("agility_attribute");
    public static final AttachmentType<Long> INTELLIGENCE_ATTACHMENT = register("intelligence_attribute");
    public static final AttachmentType<Long> WILL_ATTACHMENT = register("will_attribute");
    public static final AttachmentType<Long> FAITH_ATTACHMENT = register("faith_attribute");

    public static AttributeType fromInt(int i){
       switch (i){
           case 0 -> {
               return Strength;
           }
           case 1 -> {
               return Vitality;
           }
           case 2 -> {
               return Dexterity;
           }
           case 3 -> {
               return Agility;
           }
           case 4 -> {
               return Intelligence;
           }
           case 5 -> {
               return Will;
           }
           case 6 -> {
               return Faith;
           }
           default -> throw new IllegalStateException("Invalid Attribute Type: "+i);
       }
    }

    public AttachmentType<Long> toValueAttachment() {
        switch (this){
            case Strength -> {
               return STRENGTH_ATTACHMENT;
            }
            case Vitality -> {
                return VITALITY_ATTACHMENT;
            }
            case Dexterity -> {
                return DEXTERITY_ATTACHMENT;
            }
            case Agility -> {
                return AGILITY_ATTACHMENT;
            }
            case Intelligence -> {
                return INTELLIGENCE_ATTACHMENT;
            }
            case Will -> {
                return WILL_ATTACHMENT;
            }
            case Faith -> {
                return FAITH_ATTACHMENT;
            }
        }
        return null;
    }

    public long defaultValue() {
        if(this == Vitality){
            return 10;
        }
        return 0;
    }
}
