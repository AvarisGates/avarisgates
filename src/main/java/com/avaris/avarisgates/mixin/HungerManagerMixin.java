package com.avaris.avarisgates.mixin;

import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Inject(method = "update",at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;heal(F)V",shift = At.Shift.BEFORE), cancellable = true)
    public void update(ServerPlayerEntity player, CallbackInfo ci) {
        player.heal(
                Math.max(0,Math.min(Attribute.getAttributeWithEffects(player, AttributeType.Vitality).getValue() * 0.01f,1000.f))
        );
       ci.cancel();
    }
}
