package com.avaris.averisgates.core.player;

import com.avaris.averisgates.Averisgates;
import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.averisgates.core.player.ability.PlayerClassAbility;
import com.avaris.averisgates.core.player.ability.PlayerClassAbilityType;
import com.avaris.averisgates.core.player.player_class.PlayerClass;
import com.avaris.averisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerManager {

    public static void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData) {
        attach(player, PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        attach(player,PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT, PlayerClassType.Warrior);

        attach(player, PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0, PlayerClassAbilityType.Teleport);
        attach(player,PlayerClassAbility.PLAYER_CLASS_ABILITY_NTT_ATTACHMENT_0,0L);
    }

    private static <T> void attach(ServerPlayerEntity player, AttachmentType<T> type, T defaultValue){
        T attached = player.getAttached(type);
        if(attached == null){
            player.setAttached(type,defaultValue);
            Averisgates.LOGGER.info("Attached '{}' value='{}' to player - '{}'",type.identifier(),defaultValue,player.getNameForScoreboard());
        }else{
            Averisgates.LOGGER.info("Found attached '{}' value='{}' to player - '{}'",type.identifier(),attached,player.getNameForScoreboard());
        }

    }

    public static void receiveAbilityPacket(CastPlayerClassAbilityC2S packet, ServerPlayNetworking.Context context) {
        Averisgates.LOGGER.info("Server got ability packet: '{}', from player '{}'",packet.ability().name(),context.player().getNameForScoreboard());
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
        return null;
    }
}
