package com.avaris.avarisgates.common.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.common.currency.CurrencyAttachment;
import com.avaris.avarisgates.common.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.common.network.RequestAttributeIncrementC2S;
import com.avaris.avarisgates.common.player.ability.*;
import com.avaris.avarisgates.common.player.attribute.Attribute;
import com.avaris.avarisgates.common.player.party.PartyManager;
import com.avaris.avarisgates.common.player.party.PlayerParty;
import com.avaris.avarisgates.common.player.player_class.PlayerClass;
import com.avaris.avarisgates.common.player.player_class.PlayerClassType;
import com.avaris.towncrier.api.v1.impl.PlayerEvents;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.net.SocketAddress;
import java.util.UUID;

public class PlayerManager {

    public static void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData) {
        ensureAttached(player, PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        ensureAttached(player,PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT, PlayerClassType.Warrior);

        AttachedAbility.initForPlayer(player);

        Attribute.initForPlayer(player);

        ManaAttachment.initForPlayer(player);

        CurrencyAttachment.getCurrency(player);

        ExperienceAttachment.initForPlayer(player);
    }

    private static Text onCanJoin(SocketAddress address, GameProfile profile) {
        return null;
    }

    private static boolean onServerSendChatMessage(MinecraftServer server, ClientConnection connection, ChatMessageS2CPacket message) {
        ServerPlayerEntity player = server.getPlayerManager().getPlayer(message.sender());
        if(player != null){
            AvarisGates.LOGGER.info("Server broadcasting chat message {}: {}",player.getNameForScoreboard(),message.body().content());
        }else{
            AvarisGates.LOGGER.info("Server sends chat message: {}",message.body().content());
        }
        return true;
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

    private static void onPlayerGotKill(ServerPlayerEntity player, LivingEntity entity) {
        UUID party = PartyManager.getPlayerParty(player.getUuid());
        if(party == null){
            ExperienceAttachment.grantXp(player,entity);
            return;
        }
        PlayerParty partyInstance = PartyManager.getPartyInstance(party);
        for(var i : partyInstance.getPlayers()){
            ExperienceAttachment.grantXp(player,entity);
        }

    }

    //TODO: add check, this is for debug only!!!!
    public static void receiveAttributeIncrement(RequestAttributeIncrementC2S packet, ServerPlayNetworking.Context context) {
        long newValue = Attribute.getAttributeValue(context.player(), packet.type()).getValue() + 1;
        Attribute.setAttribute(context.player(),packet.type(),newValue);
        context.player().sendMessage(Text.literal(packet.type().name() + " set to " + newValue));
    }

    public static void init() {
        PlayerEvents.PLAYER_JOIN_EX_EVENT.register(PlayerManager::onPlayerConnect);
        PlayerEvents.CAN_JOIN_EVENT.register(PlayerManager::onCanJoin);
        PlayerEvents.SERVER_SEND_CHAT_MESSAGE_EVENT.register(PlayerManager::onServerSendChatMessage);
        PlayerEvents.PLAYER_GOT_KILL_EVENT.register(PlayerManager::onPlayerGotKill);
    }

}
