package com.avaris.avarisgates.client;

import com.avaris.avarisgates.AvarisGates;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.client.gui.screen.ingame.HandledScreen.BACKGROUND_TEXTURE;

public class InventoryRenderer {

    private static int selectedTab = 0;

    private static final int SELECTED_COLOR = Colors.WHITE;
    private static final int UNSELECTED_COLOR = Colors.LIGHT_GRAY;
    private static final Identifier BG_TEXTURE = AvarisGates.id("textures/gui/bg.png");
    private static final Identifier BG_TEXTURE1 = AvarisGates.id("textures/gui/bg1.png");

    public static int backgroundWidth = 176;
    public static int backgroundHeight = 166;

    private static int X = 0;
    private static int Y = 0;

    public static void render(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        renderTabs(context,x,y,mouseX,mouseY,delta);
        if(selectedTab == 0){
            context.drawTexture(RenderLayer::getGuiTextured, BACKGROUND_TEXTURE, x, y, 0.0F, 0.0F, InventoryRenderer.backgroundWidth, InventoryRenderer.backgroundHeight, 256, 256);
        }if(selectedTab == 1){
            context.drawTexture(RenderLayer::getGuiTextured, BG_TEXTURE, x, y, 0.0F, 0.0F, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
        }else if(selectedTab == 2){
            context.drawTexture(RenderLayer::getGuiTextured, BG_TEXTURE1, x, y, 0.0F, 0.0F, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
        }
        renderTab(context,x,y,mouseX,mouseY,delta,selectedTab);
        if(selectedTab != 0){
            renderBody(context,x,y,mouseX,mouseY,delta);
        }
    }

    private static void renderBody(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        
    }

    public static void renderTabs(DrawContext context,int x,int y,int mouseX,int mouseY,float delta){
        if(selectedTab != 0){
            renderTab(context,x,y,mouseX,mouseY,delta,0);
        }
        if(selectedTab != 1){
            renderTab(context,x,y,mouseX,mouseY,delta,1);
        }
        if(selectedTab != 2){
            renderTab(context,x,y,mouseX,mouseY,delta,2);
        }
    }

    public static void renderTab(DrawContext context,int x,int y,int mouseX,int mouseY,float delta,int idx){
        x += 7;
        y -= 14;
        X = x;
        Y = y;
        x += idx * (16 + 2);
        int color = selectedTab == idx ? SELECTED_COLOR : UNSELECTED_COLOR;
        context.fill(x,y+1,x + 16,y + 15, color);
        context.drawText(MinecraftClient.getInstance().textRenderer, Text.literal("B"+(idx+1)),x + 2,y + 5,Colors.BLACK,false);
        context.drawHorizontalLine(x+1,x+13,y,Colors.WHITE);
        context.drawVerticalLine(x,y,y + 14,Colors.WHITE);
        context.drawVerticalLine(x + 15,y,y + 15,Colors.BLACK);
    }

    public static void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if(button != GLFW.GLFW_MOUSE_BUTTON_1){
           return;
        }
        int x = X;
        int y = Y;
        if(x <= mouseX&&x >= mouseX - 16&&y <= mouseY&&y >= mouseY - 16){
            selectedTab = 0;
        }
        x += 16 + 2;
        if(x <= mouseX&&x >= mouseX - 16&&y <= mouseY&&y >= mouseY - 16){
            selectedTab = 1;
        }
        x += 16 + 2;
        if(x <= mouseX&&x >= mouseX - 16&&y <= mouseY&&y >= mouseY - 16){
            selectedTab = 2;
        }

    }

    public static int getSelectedTab() {
        return selectedTab;
    }
}
