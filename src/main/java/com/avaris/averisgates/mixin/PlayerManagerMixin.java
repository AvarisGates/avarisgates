package com.avaris.averisgates.mixin;

import com.avaris.averisgates.core.PlayerClass;
import com.avaris.averisgates.core.PlayerClassType;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ConnectedClientData;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect",at = @At("RETURN"))
    void onCreatePlayer(ClientConnection connection, ServerPlayerEntity player, ConnectedClientData clientData, CallbackInfo ci){
        player.setAttached(PlayerClass.PLAYER_EXPERIENCE_ATTACHMENT,0L);
        player.setAttached(PlayerClass.PLAYER_CLASS_TYPE_ATTACHMENT, PlayerClassType.Warrior);
    }
}
