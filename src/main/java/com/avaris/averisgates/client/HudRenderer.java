package com.avaris.averisgates.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class HudRenderer {
    public static void renderExperience(DrawContext context, RenderTickCounter tickCounter) {
        String expString = String.valueOf(AverisgatesClient.getLevel());
        int x = (context.getScaledWindowWidth() - MinecraftClient.getInstance().textRenderer.getWidth(expString)) / 2;
        int y = 8;
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal(expString),x, y, Colors.GREEN,true);
        y = 1;
        int screenWidth = context.getScaledWindowWidth() / 2;
        int width = 200;

        context.fill(screenWidth - width / 2 + 1,y + 1,screenWidth + width / 2 - 1,y + 5,Colors.BLACK);

        context.fill(screenWidth - width / 2 + 2,y + 2,screenWidth + width / 2 - 2,y + 4,Colors.YELLOW);
    }

    public static void renderHealthBar(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking) {
        final int width = 80;
        y += 6;
        context.fill(x,y,x + width,y + 8,Colors.BLACK);
        float pixels = (health / maxHealth) * width;
        context.fill(x + 1,y + 1, x + (int)pixels - 1,y + 7,Colors.LIGHT_RED);

        pixels += ((lastHealth - health) / maxHealth) * width;
        context.fill(x + 1,y + 1, x + (int)pixels - 1,y + 7,Colors.RED);
    }

    public static void renderFood(DrawContext context, PlayerEntity player, int y, int x) {
        final int width = 80;
        y += 6;
        x -= 1;
        context.fill(x,y,x - width,y + 8,Colors.BLACK);
        int pixels = (1 / 1) * width;
        context.fill(x - 1,y + 1, x - pixels + 1,y + 7,Colors.CYAN);
    }
}
