package com.avaris.avarisgates.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class InteractionManagerMixin {
    @Inject(method = "hasExperienceBar",at = @At("HEAD"),cancellable = true)
    void hasExperienceBar(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(MinecraftClient.isHudEnabled());
    }
}
