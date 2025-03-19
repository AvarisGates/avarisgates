package com.avaris.avarisgates.client.render;

import com.avaris.avarisgates.client.AvarisGatesClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;

public class ChatHudRenderer {
    public static int onRender(DrawContext context, int currentTick, int mouseX, int mouseY, boolean focused, int y,int backgroundColor) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text text = AvarisGatesClient.getChatChannelName();
        int padding = 8;
        int x = 0;
        y = y - textRenderer.fontHeight - padding / 3 - 1;

        int width = textRenderer.getWidth(text);
        int height = textRenderer.fontHeight;

        int ret = x + width + padding / 3;
        context.fill(x - padding / 3,y - padding / 3,ret,y+height + padding / 3 - 1,backgroundColor);
        context.drawText(textRenderer,text,x,y, Colors.WHITE,true);

        if(isHovered(width,height,x,y,mouseX,mouseY)){
            renderTooltip(context,textRenderer,mouseX,mouseY,backgroundColor);
        }
        return x + width + padding / 3 + 4;
    }

    private static void renderTooltip(DrawContext context,TextRenderer textRenderer, int mouseX, int mouseY,int backgroundColor) {
        int padding = 3;
        Text text = AvarisGatesClient.getChatChannelDescription();


        int width = textRenderer.getWidth(text);
        int height = textRenderer.fontHeight;

        int x = mouseX - 2;
        int y = mouseY - height + 3 * padding;

        context.fill(RenderLayer.getGuiOverlay(),x,y,x + width + 2 * padding,y - height - 2 * padding,backgroundColor);
        x = x + padding;
        y = mouseY - 2 * height + 2 * padding;
        context.drawText(textRenderer,text,x,y,Colors.WHITE,true);
    }

    public static boolean isHovered(int width,int height, int x, int y,int mouseX,int mouseY){
        return mouseX >= x&&mouseX <= x + width&&mouseY >= y&&mouseY <= y + height;
    }
}
