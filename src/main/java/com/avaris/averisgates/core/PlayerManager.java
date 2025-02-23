package com.avaris.averisgates.core;

import com.avaris.averisgates.Averisgates;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerManager {

    public static void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData) {
        attach(player,PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        attach(player,PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT,PlayerClassType.Warrior);

        attach(player,PlayerClassAbility.PLAYER_CLASS_ABILITY_TYPE_ATTACHMENT_0,PlayerClassAbilityType.Teleport);
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
}
