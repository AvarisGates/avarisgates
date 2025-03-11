package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.AvarisGatesClient;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {


    @Inject(method = "doAttack",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;resetLastAttackedTicks()V"))
    void onMissedDoAttack(CallbackInfoReturnable<Boolean> cir, @Local ItemStack stack){
        AvarisGatesClient.onMissedDoAttack(((MinecraftClient) (Object)this).player,stack);
    }
}
