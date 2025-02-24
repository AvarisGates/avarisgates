package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.InventoryRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.client.gui.screen.ingame.HandledScreen.BACKGROUND_TEXTURE;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @Inject(method = "render",at = @At("HEAD"),cancellable = true)
    void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        if(InventoryRenderer.getSelectedTab() != 0){
            InventoryScreen this_ = (InventoryScreen)(Object) this;
            int x = this_.x;
            int y = this_.y;
            this_.renderInGameBackground(context);
            InventoryRenderer.render(context,x,y,mouseX,mouseY,delta);
            ci.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "drawBackground",cancellable = true)
    private void drawBackground(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo ci) {
        InventoryScreen this_ = (InventoryScreen)(Object) this;
        int x = this_.x;
        int y = this_.y;
        InventoryRenderer.render(context,x,y,mouseX,mouseY,delta);
        InventoryScreen.drawEntity(context, x + 26, y + 8, x + 75, y + 78, 30, 0.0625F, mouseX, mouseY, MinecraftClient.getInstance().player);
        ci.cancel();
    }
}
