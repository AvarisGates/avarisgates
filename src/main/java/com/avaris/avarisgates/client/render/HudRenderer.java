package com.avaris.avarisgates.client.render;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.client.AvarisGatesClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class HudRenderer {

    private static final Identifier HUD_TEXTURES = AvarisGates.id("textures/hud/bar_hp_mp.png");

    public static void renderExperience(DrawContext context, RenderTickCounter tickCounter) {
        String expString = String.valueOf(AvarisGatesClient.getLevel());
        int x = (context.getScaledWindowWidth() - MinecraftClient.getInstance().textRenderer.getWidth(expString)) / 2;
        int y = 8;
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(expString),x, y, Colors.GREEN,true);
        x = context.getScaledWindowWidth();
        y = 1;
        int screenWidth = context.getScaledWindowWidth() / 2;
        int width = 158;

        //context.fill(screenWidth - width / 2 + 1,y + 1,screenWidth + width / 2 - 1,y + 5,Colors.BLACK);

        context.fill(screenWidth - width / 2 + 4,y + 2,screenWidth + width / 2,y + 6,0xFF_5F_F1_5F);
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, HUD_TEXTURES, screenWidth - width + 20, y - 21, 0.0F, 0.0F, 220, 100, 450, 128);
    }

    public static void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean healthDecreased) {
        final int width = 78;
        y += 6;

        if(player.isAlive()){
            //Health before last damage instance
            float pixels = (health / maxHealth) * width;
            if(healthDecreased){
                context.fill(x + 1,y + 1, x + (int)pixels,y + 7,0xAF_FF_0F_0F);
            }
            //Health after last damage instance
            pixels += ((lastHealth - health) / maxHealth) * width;
            context.fill(x + 1,y + 1, x + (int)pixels,y + 7,0xFF_a80c0c);


            TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

            //We have to use player.getHealth to get the most recent, not rounded value
            String s = ((int)((player.getHealth() / maxHealth) * 100)) + "%";
            context.drawText(textRenderer, Text.literal(s),x + width / 2 - textRenderer.getWidth(s) / 2,y - 10,Colors.WHITE,true);
        }
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, HUD_TEXTURES, x - 30, y - 45, 0.0F, 0.0F, 110, 59, 225, 256);
    }

    public static void renderMana(DrawContext context, PlayerEntity player, int y, int x) {
        long mana = AvarisGatesClient.getMana();
        long maxMana = AvarisGatesClient.getMaxMana();
        String s = ((int)(((float)mana / maxMana) * 100)) + "%";
        final int width = 80;
        y += 6;
        x -= 1;
        float pixels = ((float) mana / maxMana) * width;
        context.fill(x - 2, y + 1, (int) (x - pixels), y + 7, 0xFF_3476d7);
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, HUD_TEXTURES, x - 110, y - 45, 0.0F, 0.0F, 110, 59, 225, 256);

        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        context.drawText(textRenderer, Text.literal(s),x - width / 2 - textRenderer.getWidth(s) / 2,y - 10,Colors.WHITE,true);
    }
}
