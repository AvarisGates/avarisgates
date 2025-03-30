package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.render.HudRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow private long heartJumpEndTick;

    @Shadow private int ticks;

    @Inject(method = "renderExperienceLevel",at = @At("HEAD"),cancellable = true)
    void onRenderExperience(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        ci.cancel();
        Profilers.get().push("expLevel");
        HudRenderer.renderExperience(context,tickCounter);
        Profilers.get().pop();
    }

    @Inject(method = "renderHealthBar",at = @At("HEAD"),cancellable = true)
    void onRenderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci){
        ci.cancel();
        boolean renderDecreasedHealth = this.heartJumpEndTick >= this.ticks;
        HudRenderer.renderHealthBar(context,
                player,
                x,
                y,
                lines,
                regeneratingHeartIndex,
                maxHealth,
                lastHealth,
                health,
                absorption,
                renderDecreasedHealth
        );
    }

    @Inject(method = "shouldRenderExperience",at = @At("HEAD"),cancellable = true)
    void shouldRenderExperience(CallbackInfoReturnable<Boolean> cir){
       cir.setReturnValue(false);
    }

    @Inject(method = "renderFood",at = @At("HEAD"),cancellable = true)
    void onRenderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci){
        HudRenderer.renderMana(context,player,top,right);
        ci.cancel();
    }
    @Inject(method = "renderArmor", at=@At("HEAD"),cancellable = true)
    private static void onRenderArmor(DrawContext context, PlayerEntity player, int i, int j, int k, int x, CallbackInfo ci){
       ci.cancel();
    }

    @Inject(method = "renderAirBubbles", at = @At("HEAD"),cancellable = true)
    void onRenderAirBubbles(DrawContext context, PlayerEntity player, int heartCount, int top, int left, CallbackInfo ci){
        ci.cancel();
    }

}
