package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.InventoryRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "render",at = @At("RETURN"))
    void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci){
        InventoryScreen this_ = (InventoryScreen)(Object) this;
        int x = this_.x;
        int y = this_.y;
        InventoryRenderer.render(context,x,y,mouseX,mouseY,delta);
    }
}
