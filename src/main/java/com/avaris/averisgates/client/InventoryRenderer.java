package com.avaris.averisgates.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class InventoryRenderer {
    public static void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        x += 7;
        y -= 16;
        context.fill(x,y,x + 16,y + 16, Colors.WHITE);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("B1"),x + 3,y + 5,Colors.BLACK,false);

        x += 16 + 2;
        context.fill(x,y,x + 16,y + 16, Colors.WHITE);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("B2"),x + 3,y + 5,Colors.BLACK,false);

        x += 16 + 2;
        context.fill(x,y,x + 16,y + 16, Colors.WHITE);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("B3"),x + 3,y + 5,Colors.BLACK,false);
    }
}
