package com.avaris.avarisgates.mixin;

import com.avaris.avarisgates.core.api.event.PlayerEvents;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.SocketAddress;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect",at = @At("RETURN"))
    void onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){
        // PLAYER_JOIN_EX_EVENT implicitly calls PLAYER_JOIN_EVENT
        PlayerEvents.PLAYER_JOIN_EX_EVENT.invoker().onPlayerJoin(connection,player,clientData);
    }

    @Inject(method = "remove", at = @At("HEAD"))
    void onPlayerDisconnect(ServerPlayerEntity player, CallbackInfo ci){
       PlayerEvents.PLAYER_LEAVE_EVENT.invoker().onPlayerLeave(player);
    }

    @Inject(method = "checkCanJoin",at = @At("RETURN"),cancellable = true)
    void onCanJoin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir){
        if(cir.getReturnValue() != null){
            return;
        }

        Text ret = PlayerEvents.CAN_JOIN_EVENT.invoker().onCanJoin(address,profile);
        if(ret != null){
            cir.setReturnValue(ret);
        }
    }
}
