package com.avaris.avarisgates.core.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbility;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import com.avaris.avarisgates.core.player.player_class.PlayerClass;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerManager {

    public static void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData) {
        ensureAttached(player, PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        ensureAttached(player,PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT, PlayerClassType.Warrior);

        ensureAttached(player, PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0, PlayerClassAbilityType.Teleport);
        ensureAttached(player, PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1, PlayerClassAbilityType.Cleave);
        ensureAttached(player, PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2, PlayerClassAbilityType.Whirlwind);

        Long attached = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0);
        if(attached == null||attached > player.server.getTicks()){
            player.setAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0,0L);
        }

        Long attached1 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1);
        if(attached1 == null||attached1 > player.server.getTicks()){
            player.setAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1,0L);
        }

        Long attached2 = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2);
        if(attached2 == null||attached2 > player.server.getTicks()){
            player.setAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2,0L);
        }

        ServerPlayNetworking.send(player,new ChangeAbilityS2C(0,player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0)));
        ServerPlayNetworking.send(player,new ChangeAbilityS2C(1,player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1)));
        ServerPlayNetworking.send(player,new ChangeAbilityS2C(2,player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2)));
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
        PlayerClassAbility ability = getAttachedAbility(context.player(), packet.ability());
        if(ability == null){
            return;
        }
        if(ability.getCooldown(context.server().getTicks()) <= 0){
            ability.trigger(context.server(),context.player());
        }

    }

    private static PlayerClassAbility getAttachedAbility(ServerPlayerEntity player, PlayerClassAbilityType type) {
        PlayerClassAbilityType newType = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0);
        if(newType == type){
            //Next trigger time - when the ability can be used next time (in ticks)
            Long ntt = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0);
            return PlayerClassAbility.build(newType,ntt,PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0);
        }

        newType = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_1);
        if(newType == type){
            //Next trigger time - when the ability can be used next time (in ticks)
            Long ntt = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1);
            return PlayerClassAbility.build(newType,ntt,PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_1);
        }

        newType = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_2);
        if(newType == type){
            //Next trigger time - when the ability can be used next time (in ticks)
            Long ntt = player.getAttached(PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2);
            return PlayerClassAbility.build(newType,ntt,PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_2);
        }
        return null;
    }
}
