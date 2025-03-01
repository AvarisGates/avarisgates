package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;

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

    public static void initForPlayer(ServerPlayerEntity player) {
        AttachedAbility ability0 = AttachedAbility.getAttached(player, AbilitySlot.SLOT0);
        if(ability0.getType() == null||ability0.getNtt() == null){
            AttachedAbility.setAttached(player,new AttachedAbility(PlayerClassAbilityType.Teleport,0L,AbilitySlot.SLOT0));
        }else if(ability0.getNtt() > player.server.getTicks()){
            ability0.setNtt(0L);
            AttachedAbility.setAttached(player,ability0);
        }

        AttachedAbility ability1 = AttachedAbility.getAttached(player, AbilitySlot.SLOT1);
        if(ability1.getType() == null || ability1.getNtt() == null){
            AttachedAbility.setAttached(player,new AttachedAbility(PlayerClassAbilityType.Cleave,0L,AbilitySlot.SLOT1));
        } else if(ability1.getNtt() > player.server.getTicks()){
            ability1.setNtt(0L);
            AttachedAbility.setAttached(player,ability1);
        }

        AttachedAbility ability2 = AttachedAbility.getAttached(player, AbilitySlot.SLOT2);
        if(ability2.getType() == null||ability2.getNtt() == null){
            AttachedAbility.setAttached(player,new AttachedAbility(PlayerClassAbilityType.Whirlwind,0L,AbilitySlot.SLOT2));
        } else if(ability2.getNtt() > player.server.getTicks()){
            ability2.setNtt(0L);
            AttachedAbility.setAttached(player,ability2);
        }

        ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,AttachedAbility.getAttached(player,AbilitySlot.SLOT0).getType()));
        ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,AttachedAbility.getAttached(player,AbilitySlot.SLOT1).getType()));
        ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,AttachedAbility.getAttached(player,AbilitySlot.SLOT2).getType()));
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
