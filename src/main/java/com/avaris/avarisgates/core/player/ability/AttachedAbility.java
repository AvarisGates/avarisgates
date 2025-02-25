package com.avaris.avarisgates.core.player.ability;

import net.minecraft.entity.LivingEntity;

// Use this class to get/set attached abilities to players/entities
// This includes the ability type and cooldown in the future more info will be added
public class AttachedAbility {
    private final AbilitySlot slot;
    private final PlayerClassAbilityType type;

    private Long ntt;

    public AttachedAbility(PlayerClassAbilityType type, Long ntt,AbilitySlot slot) {
        this.type = type;
        this.ntt = ntt;
        this.slot = slot;
    }

    public static AttachedAbility getAttached(LivingEntity entity, AbilitySlot slot){
        PlayerClassAbilityType type = entity.getAttached(slot.toAttachmentType());
        Long ntt = entity.getAttached(slot.toNttAttachment());
        return new AttachedAbility(type,ntt,slot);
    }

    public static void setAttached(LivingEntity entity,AttachedAbility ability){
        entity.setAttached(ability.slot.toAttachmentType(),ability.type);
        entity.setAttached(ability.slot.toNttAttachment(),ability.ntt);
    }

    public Long getNtt() {
        return this.ntt;
    }

    public AbilitySlot getSlot() {
        return slot;
    }

    public PlayerClassAbilityType getType() {
        return type;
    }

    public void setNtt(Long ntt) {
        this.ntt = ntt;
    }
}
