package com.avaris.avarisgates.client;

import com.avaris.avarisgates.AvarisGates;
import net.minecraft.client.MinecraftClient;
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

    public static void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
        final int width = 80;
        y += 6;

        //Health before last damage instance
        float pixels = (health / maxHealth) * width;
        context.fill(x + 1,y + 1, x + (int)pixels - 1,y + 7,Colors.LIGHT_RED);
        //Health after last damage instance
        pixels += ((lastHealth - health) / maxHealth) * width;
        context.fill(x + 1,y + 1, x + (int)pixels - 1,y + 7,0xFF_a80c0c);
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, HUD_TEXTURES, x - 30, y - 45, 0.0F, 0.0F, 110, 59, 225, 256);
    }

    public static void renderFood(DrawContext context, PlayerEntity player, int y, int x) {
        final int width = 80;
        y += 6;
        x -= 1;
        int pixels = (1 / 1) * width;
        context.fill(x - 1,y + 1, x - pixels + 1,y + 7,0xFF_3476d7);
        context.drawTexture(RenderLayer::getGuiTexturedOverlay, HUD_TEXTURES, x - 110, y - 45, 0.0F, 0.0F, 110, 59, 225, 256);
    }
}
