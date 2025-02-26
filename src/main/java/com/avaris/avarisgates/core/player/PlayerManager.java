package com.avaris.avarisgates.core.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.AttributeIncrementS2C;
import com.avaris.avarisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.network.RequestAttributeIncrementC2S;
import com.avaris.avarisgates.core.player.ability.*;
import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import com.avaris.avarisgates.core.player.player_class.PlayerClass;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PlayerManager {

    public static void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData) {
        ensureAttached(player, PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        ensureAttached(player,PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT, PlayerClassType.Warrior);

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

        for(AttributeType type : AttributeType.values()){
            Attribute attribute = Attribute.getAttribute(player,type);
            ServerPlayNetworking.send(player,new AttributeIncrementS2C(type,attribute.getValue()));
            AvarisGates.LOGGER.info("{}",attribute);
        }
    }

    private static <T> void ensureAttached(ServerPlayerEntity player, AttachmentType<T> type, T defaultValue){
        T attached = player.getAttached(type);
        if(attached == null){
            player.setAttached(type,defaultValue);
            AvarisGates.LOGGER.info("Attached '{}' value='{}' to player - '{}'",type.identifier(),defaultValue,player.getNameForScoreboard());
        }else{
            AvarisGates.LOGGER.info("Found attached '{}' value='{}' to player - '{}'",type.identifier(),attached,player.getNameForScoreboard());
        }

    }

    public static void receiveAbilityPacket(CastPlayerClassAbilityC2S packet, ServerPlayNetworking.Context context) {
        AvarisGates.LOGGER.info("Server got ability packet: '{}', from player '{}'",packet.ability().name(),context.player().getNameForScoreboard());
        AttachedAbility ability0 = AttachedAbility.getAttached(context.player(), AbilitySlot.SLOT0);
        AttachedAbility ability1 = AttachedAbility.getAttached(context.player(), AbilitySlot.SLOT1);
        AttachedAbility ability2 = AttachedAbility.getAttached(context.player(), AbilitySlot.SLOT2);

        PlayerClassAbility<?> ability = null;
        if(ability0.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability0.getType(),ability0);
        }
        if(ability1.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability1.getType(),ability1);
        }
        if(ability2.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability2.getType(),ability2);
        }
        if(ability == null){
            return;
        }
        if(ability.getCooldown(context.server().getTicks()) <= 0){
            ability.trigger(context.server(),context.player());
        }

    }

    public static void receiveAttributeIncrement(RequestAttributeIncrementC2S packet, ServerPlayNetworking.Context context) {
        long newValue = Attribute.getAttribute(context.player(), packet.type()).getValue() + 1;
        Attribute.setAttribute(context.player(),packet.type(),newValue);
        context.player().sendMessage(Text.literal(packet.type().name() + " set to " + newValue));
    }
}
