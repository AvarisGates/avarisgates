package com.avaris.avarisgates.core.api.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;

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
}
