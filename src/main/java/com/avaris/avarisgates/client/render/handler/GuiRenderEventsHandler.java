package com.avaris.avarisgates.client.render.handler;

import com.avaris.avarisgates.client.render.InventoryRenderer;
import com.avaris.towncrier.client.api.v1.impl.GuiInputEvents;
import com.avaris.towncrier.client.api.v1.impl.GuiRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;

public class GuiRenderEventsHandler {
    public static void init(){
        GuiInputEvents.INVENTORY_MOUSE_CLICKED_EVENT.register(GuiRenderEventsHandler::onMouseClicked);
        GuiRenderEvents.RENDER_INVENTORY_SCREEN_EVENT.register(GuiRenderEventsHandler::renderInventoryScreen);
        GuiRenderEvents.RENDER_INVENTORY_SCREEN_BACKGROUND_EVENT.register(GuiRenderEventsHandler::renderInventoryScreenBackground);
    }

    private static boolean onMouseClicked(InventoryScreen screen, double mouseX, double mouseY, int button){
        InventoryRenderer.mouseClicked(mouseX,mouseY,button);
        return InventoryRenderer.getSelectedTab() <= 0;
    }
    private static boolean renderInventoryScreen(InventoryScreen screen, DrawContext context, int mouseX, int mouseY, float delta) {
        if(InventoryRenderer.getSelectedTab() != 0){
            int x = screen.x;
            int y = screen.y;
            screen.renderInGameBackground(context);
            InventoryRenderer.render(context,x,y,mouseX,mouseY,delta);
            return false;
        }
        return true;
    }

    private static boolean renderInventoryScreenBackground(InventoryScreen screen,DrawContext context, int mouseX, int mouseY, float delta) {
        int x = screen.x;
        int y = screen.y;
        InventoryRenderer.render(context,x,y,mouseX,mouseY,delta);
        InventoryScreen.drawEntity(context,
                x + 26,
                y + 8,
                x + 75,
                y + 78,
                30,
                0.0625F,
                mouseX,
                mouseY,
                MinecraftClient.getInstance().player);
        return false;
    }

}
