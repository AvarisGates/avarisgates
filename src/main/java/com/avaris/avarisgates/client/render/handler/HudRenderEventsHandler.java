package com.avaris.avarisgates.client.render.handler;

import com.avaris.avarisgates.client.render.HudRenderer;
import com.avaris.towncrier.client.api.v1.impl.HudRenderEvents;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;

public class HudRenderEventsHandler {
    public static void init(){
        HudRenderEvents.RENDER_EXPERIENCE_EVENT.register(HudRenderEventsHandler::renderExperience);
        HudRenderEvents.RENDER_HEALTH_BAR_EVENT.register(HudRenderEventsHandler::renderHealth);
        HudRenderEvents.SHOULD_RENDER_EXPERIENCE_EVENT.register(HudRenderEventsHandler::shouldRenderExperienceBar);
        HudRenderEvents.RENDER_FOOD_EVENT.register(HudRenderEventsHandler::renderFoodBar);
        HudRenderEvents.RENDER_ARMOR_EVENT.register(HudRenderEventsHandler::renderArmor);
        HudRenderEvents.RENDER_AIR_BUBBLES_EVENT.register(HudRenderEventsHandler::renderAirBubbles);
    }

    public static boolean renderExperience(DrawContext context, RenderTickCounter tickCounter) {
        HudRenderer.renderExperience(context,tickCounter);
        return false;
    }

    private static boolean renderHealth(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean renderDecreasedHealth) {
        HudRenderer.renderHealthBar(context,
                player,
                x,
                y,
                lines,
                regeneratingHeartIndex,
                maxHealth,
                lastHealth,
                health,
                absorption,
                renderDecreasedHealth
        );
        return false;
    }

    private static boolean renderAirBubbles(DrawContext context, PlayerEntity player, int heartCount, int top, int left) {
        return false;
    }

    private static boolean renderArmor(DrawContext context, PlayerEntity player, int i, int j, int k, int x) {
        return false;
    }

    private static boolean renderFoodBar(DrawContext context, PlayerEntity player, int top, int right) {
        HudRenderer.renderMana(context,player,top,right);
        return false;
    }

    private static boolean shouldRenderExperienceBar() {
        return false;
    }
}
