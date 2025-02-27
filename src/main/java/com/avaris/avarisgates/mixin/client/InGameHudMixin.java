package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.HudRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.profiler.Profilers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

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
        HudRenderer.renderHealthBar(context,player,x,y,lines,regeneratingHeartIndex,maxHealth,lastHealth,health,absorption,blinking);
        //InGameHud.HeartType heartType = InGameHud.HeartType.fromPlayerState(player);
        //boolean bl = player.getWorld().getLevelProperties().isHardcore();
        //int i = MathHelper.ceil((double)maxHealth / 2.0);
        //int j = MathHelper.ceil((double)absorption / 2.0);
        //int k = i * 2;

        //for (int l = i + j - 1; l >= 0; l--) {
        //    int m = l / 10;
        //    int n = l % 10;
        //    int o = x + n * 8;
        //    int p = y - m * lines;
        //    if (lastHealth + absorption <= 4) {
        //        p += this.random.nextInt(2);
        //    }

        //    if (l < i && l == regeneratingHeartIndex) {
        //        p -= 2;
        //    }

        //    this.drawHeart(context, InGameHud.HeartType.CONTAINER, o, p, bl, blinking, false);
        //    int q = l * 2;
        //    boolean bl2 = l >= i;
        //    if (bl2) {
        //        int r = q - k;
        //        if (r < absorption) {
        //            boolean bl3 = r + 1 == absorption;
        //            this.drawHeart(context, heartType == InGameHud.HeartType.WITHERED ? heartType : InGameHud.HeartType.ABSORBING, o, p, bl, false, bl3);
        //        }
        //    }

        //    if (blinking && q < health) {
        //        boolean bl4 = q + 1 == health;
        //        this.drawHeart(context, heartType, o, p, bl, true, bl4);
        //    }

        //    if (q < lastHealth) {
        //        boolean bl4 = q + 1 == lastHealth;
        //        this.drawHeart(context, heartType, o, p, bl, false, bl4);
        //    }
        //}
    }

    @Inject(method = "shouldRenderExperience",at = @At("HEAD"),cancellable = true)
    void shouldRenderExperience(CallbackInfoReturnable<Boolean> cir){
       cir.setReturnValue(false);
    }

    @Inject(method = "renderFood",at = @At("HEAD"),cancellable = true)
    void onRenderFood(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci){
        HudRenderer.renderFood(context,player,top,right);
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
