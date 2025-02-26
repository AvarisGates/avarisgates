package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.InventoryRenderer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeBookScreen.class)
public class RecipeBookScreenMixin {
    @Inject(method = "mouseClicked",at = @At("HEAD"),cancellable = true)
    void onMouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir){
        RecipeBookScreen<?> this_ = (RecipeBookScreen<?>) (Object) this;
        if(this_ instanceof InventoryScreen screen){
            InventoryRenderer.mouseClicked(mouseX,mouseY,button,cir);
            if(InventoryRenderer.getSelectedTab() > 0){
                cir.setReturnValue(false);
            }
        }

    }
}
