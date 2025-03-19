package com.avaris.avarisgates.core.api.event;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.net.SocketAddress;

public class PlayerEvents {
    public static final Event<PlayerJoin> PLAYER_JOIN_EVENT = EventFactory.createArrayBacked(PlayerJoin.class,(callbacks) -> (player) -> {
        for(var callback : callbacks){
            callback.onPlayerJoin(player);
        }
    });

    /**
     * Extended player join event, calls {@link PlayerEvents#PLAYER_JOIN_EVENT}
     */
    public static final Event<PlayerJoinEx> PLAYER_JOIN_EX_EVENT = EventFactory.createArrayBacked(PlayerJoinEx.class,(callbacks) -> (connection,player,clientData) -> {
        for(var callback : callbacks){
            callback.onPlayerJoin(connection,player,clientData);
        }
        PlayerEvents.PLAYER_JOIN_EVENT.invoker().onPlayerJoin(player);
    });

    public static final Event<PlayerLeave> PLAYER_LEAVE_EVENT = EventFactory.createArrayBacked(PlayerLeave.class,(callbacks) -> (player) ->{
        for(var callback : callbacks){
            callback.onPlayerLeave(player);
        }
    });

    // Return null to allow the player to join
    public static final Event<CanJoin> CAN_JOIN_EVENT = EventFactory.createArrayBacked(CanJoin.class,(callbacks) -> (address,profile) ->{
        for(var callback : callbacks){
            Text ret = callback.onCanJoin(address,profile);
            if(ret != null){
                return ret;
            }
        }
        return null;
    });
    public static final Event<ServerSendChatMessage> SERVER_SEND_CHAT_MESSAGE_EVENT = EventFactory.createArrayBacked(ServerSendChatMessage.class,(callbacks) -> (server,connection, packet) -> {
        for(var callback : callbacks){
            boolean ret = callback.onServerSendChatMessage(server,connection,packet);
            if(!ret){
                return false;
            }
        }
        return true;
    });

    @FunctionalInterface
    public interface PlayerJoin{
        void onPlayerJoin(ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface PlayerJoinEx{
        void onPlayerJoin(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData);
    }

    @FunctionalInterface
    public interface PlayerLeave{
        void onPlayerLeave(ServerPlayerEntity player);
    }

    @FunctionalInterface
    public interface CanJoin{
        Text onCanJoin(SocketAddress address, GameProfile profile);
    }

    @FunctionalInterface
    public interface ServerSendChatMessage {
       boolean onServerSendChatMessage(MinecraftServer server, ClientConnection connection, ChatMessageS2CPacket packet);
    }
}
