package com.avaris.avarisgates.mixin.client;

import com.avaris.avarisgates.client.AvarisGatesClient;
import com.avaris.avarisgates.client.render.ChatHudRenderer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.message.ChatVisibility;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ChatHud.class)
@Environment(EnvType.CLIENT)
public abstract class ChatHudMixin {
    @Shadow private int scrolledLines;
    @Shadow @Final private List<ChatHudLine.Visible> visibleMessages;
    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract boolean isChatHidden();

    @Shadow public abstract int getVisibleLineCount();

    @Shadow protected abstract int getLineHeight();

    @Shadow public abstract int getHeight();

    @Inject(method = "render",at = @At("HEAD"))
    void onRender(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, CallbackInfo ci){
        if(isChatHidden()){
            return;
        }
        Screen currentScreen =  this.client.currentScreen;
        if(!(currentScreen instanceof ChatScreen)){
            return;
        }

        int scaledHeight = MathHelper.floor((context.getScaledWindowHeight() - 40) / (float)this.getChatScale());

        // Calculate height, on top of the chat box
        //double spacing = this.client.options.getChatLineSpacing().getValue();
        //int spacingHeight = (int)Math.round(-8.0 * (spacing + 1.0) + 4.0 * spacing);
        //int index = Math.min(this.visibleMessages.size() - this.scrolledLines,this.getVisibleLineCount());
        //int y = scaledHeight - index * this.getLineHeight() + spacingHeight;

        int y = context.getScaledWindowHeight();

        // Scale matrices
        float f = (float)this.getChatScale();

        // For chat window
        context.getMatrices().push();
        context.getMatrices().scale(f, f, 1.0F);
        context.getMatrices().translate(4.0F, 0.0F, 0.0F);

        // For messages
        context.getMatrices().translate(0.0F, 0.0F, 50.0F);

        //Calculate background color

        double e = this.client.options.getTextBackgroundOpacity().getValue();
        int v = (int)(255.0 * e);
        int backgroundColor = v << 24;

        int width = ChatHudRenderer.onRender(context,
                currentTick,
                // Scale mouse position to match the matrix
                (int) (mouseX / f),
                (int) (mouseY / f),
                focused,
                y,
                backgroundColor);
        context.getMatrices().pop();
        context.getMatrices().translate(width,0,0);
    }

    @Shadow
    public abstract int getWidth();

    @Shadow
    public abstract double getChatScale();

    @Shadow
    private static double getMessageOpacityMultiplier(int age) {
        return 0;
    }
}
