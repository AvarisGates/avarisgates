package com.avaris.avarisgates.common.network;

import com.avaris.avarisgates.common.player.PlayerManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

// This class handles registering the mod packets
// Packet Description:
// Both packet types are required to be registered on the client and the server.
// C2S - Client to Server, a packet sent by the client, received and handled by the server.
// DON'T TRUST THE CLIENT WITH ANYTHING IMPORTANT
// Remember the client can send any data, we have to validate it and issue an adequate response.
// S2C - Server to Client, a packet sent by the server, received and handled by the client.
// We don't need to validate these packets.
// If data isn't synced properly we always prioritize the server's version of the data.
// If something isn't ABSOLUTELY required to be sent to the client we don't.
// This conserves both computing power on the server and packets sent to the client.
public class ModPackets {

    // A common (server and client side) function to register the mod packets
    public static void init(){

        PayloadTypeRegistry.playC2S().register(CastPlayerClassAbilityC2S.ID, CastPlayerClassAbilityC2S.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(CastPlayerClassAbilityC2S.ID, PlayerManager::receiveAbilityPacket);

        PayloadTypeRegistry.playS2C().register(ChangeAbilityS2C.ID, ChangeAbilityS2C.CODEC);

        PayloadTypeRegistry.playC2S().register(RequestAttributeIncrementC2S.ID, RequestAttributeIncrementC2S.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(RequestAttributeIncrementC2S.ID, PlayerManager::receiveAttributeIncrement);
    }
}
