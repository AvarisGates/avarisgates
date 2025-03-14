package com.avaris.avarisgates.core.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.currency.CurrencyAttachment;
import com.avaris.avarisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.core.network.RequestAttributeIncrementC2S;
import com.avaris.avarisgates.core.player.ability.*;
import com.avaris.avarisgates.core.player.attribute.Attribute;
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

        AttachedAbility.initForPlayer(player);

        Attribute.initForPlayer(player);

        ManaAttachment.initForPlayer(player);

        CurrencyAttachment.getCurrency(player);
    }

    private static <T> void ensureAttached(ServerPlayerEntity player, AttachmentType<T> type, T defaultValue){
        T attached = player.getAttachedOrCreate(type);
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
        AttachedAbility basic_ability = AttachedAbility.getAttached(context.player(),AbilitySlot.BASIC);

        PlayerClassAbility ability = null;
        if(ability0.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability0.getType(),ability0);
        }
        if(ability1.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability1.getType(),ability1);
        }
        if(ability2.getType() == packet.ability()){
            ability = AbilityRegistrar.build(ability2.getType(),ability2);
        }

        //Account for basic attacks
        if(basic_ability.getType() == packet.ability()&&
                PlayerClass.isBasicWeapon(context.player(),context.player().getWeaponStack())){
            ability = AbilityRegistrar.build(basic_ability.getType(),basic_ability);
        }

        if(ability == null){
            return;
        }
        if(ability.getCooldown(context.server().getTicks()) <= 0){
            ability.trigger(context.server(),context.player());
        }

    }

    //TODO: add check, this is for debug only!!!!
    public static void receiveAttributeIncrement(RequestAttributeIncrementC2S packet, ServerPlayNetworking.Context context) {
        long newValue = Attribute.getAttributeValue(context.player(), packet.type()).getValue() + 1;
        Attribute.setAttribute(context.player(),packet.type(),newValue);
        context.player().sendMessage(Text.literal(packet.type().name() + " set to " + newValue));
    }
}
