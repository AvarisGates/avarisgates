package com.avaris.avarisgates.client;

import com.avaris.avarisgates.client.keyboard.ClientKeyBinds;
import com.avaris.avarisgates.client.render.HudRenderer;
import com.avaris.avarisgates.client.render.InventoryRenderer;
import com.avaris.avarisgates.common.currency.CurrencyAttachment;
import com.avaris.avarisgates.common.entity.ModEntities;
import com.avaris.avarisgates.core.api.event.ClientLifecycleEvents;
import com.avaris.avarisgates.common.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.common.network.ChangeAbilityS2C;
import com.avaris.avarisgates.common.player.ManaAttachment;
import com.avaris.avarisgates.common.player.ability.AbilitySlot;
import com.avaris.avarisgates.common.player.ability.AttachedAbility;
import com.avaris.avarisgates.common.player.attribute.Attribute;
import com.avaris.avarisgates.common.player.attribute.AttributeType;
import com.avaris.avarisgates.common.player.player_class.PlayerClass;
import com.avaris.towncrier.client.api.v1.impl.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.RecipeBookScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

/**
 * Client mod entrypoint.
 * @see com.avaris.avarisgates.AvarisGates
 */
public class AvarisGatesClient implements ClientModInitializer {


    public static long getLevel() {
       return 300;
    }

    public static long getAttributeValue(AttributeType type) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){
            return 0;
        }
        return Attribute.getAttributeWithEffects(player, type).getValue();
    }

    public static long getMana() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){
            return 0;
        }
        ManaAttachment manaAttachment = ManaAttachment.getMana(player);
        if(manaAttachment == null){
            return 0;
        }
        return Objects.requireNonNullElse(manaAttachment.getValue(),0L);
    }

    public static long getMaxMana() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){
            return 0;
        }
        ManaAttachment manaAttachment = ManaAttachment.getMana(player);
        if(manaAttachment == null){
            return 0;
        }
        return Objects.requireNonNullElse(manaAttachment.getMaxValue(),0L);
    }

    public static long getBasicCurrency() {
        return CurrencyAttachment.getCurrency(MinecraftClient.getInstance().player).getBasicCurrency();
    }

    static void onMissedDoAttack(ClientPlayerEntity player, ItemStack stack) {
        if(PlayerClass.isBasicWeapon(player,stack)){
            ClientPlayNetworking.send(new CastPlayerClassAbilityC2S(AttachedAbility.getAttached(player, AbilitySlot.BASIC).getType()));
        }
    }

    static boolean onMouseClicked(InventoryScreen screen, double mouseX, double mouseY, int button){
        InventoryRenderer.mouseClicked(mouseX,mouseY,button);
        return InventoryRenderer.getSelectedTab() <= 0;
    }

    public static Text getChatChannelName() {
        return Text.literal("GLOBAL");
    }

    public static Text getChatChannelDescription() {
        return Text.literal("Current Channel");
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

    private static boolean hasExperienceBar(ClientPlayerInteractionManager manager) {
        return MinecraftClient.isHudEnabled();
    }

    private static boolean renderExperience(DrawContext context, RenderTickCounter tickCounter) {
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

    @Override
    public void onInitializeClient() {

        ClientLifecycleEvents.INITIALIZE_EVENT.invoker().onInitialize();

        ClientKeyBinds.init();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            ClientKeyBinds.tick(client);
        });

        ModEntities.registerEntityRenderers();

        ClientPlayNetworking.registerGlobalReceiver(ChangeAbilityS2C.ID, ClientKeyBinds::receiveChangeAbility);

        ClientPlayerEvents.PLAYER_DO_ATTACK_MISS_EVENT.register(AvarisGatesClient::onMissedDoAttack);

        GuiInputEvents.INVENTORY_MOUSE_CLICKED_EVENT.register(AvarisGatesClient::onMouseClicked);

        GuiRenderEvents.RENDER_INVENTORY_SCREEN_EVENT.register(AvarisGatesClient::renderInventoryScreen);

        GuiRenderEvents.RENDER_INVENTORY_SCREEN_BACKGROUND_EVENT.register(AvarisGatesClient::renderInventoryScreenBackground);

        InteractionManagerEvents.HAS_EXPERIENCE_BAR_EVENT.register(AvarisGatesClient::hasExperienceBar);

        HudRenderEvents.RENDER_EXPERIENCE_EVENT.register(AvarisGatesClient::renderExperience);
        
        HudRenderEvents.RENDER_HEALTH_BAR_EVENT.register(AvarisGatesClient::renderHealth);

        HudRenderEvents.SHOULD_RENDER_EXPERIENCE_EVENT.register(AvarisGatesClient::shouldRenderExperienceBar);

        HudRenderEvents.RENDER_FOOD_EVENT.register(AvarisGatesClient::renderFoodBar);

        HudRenderEvents.RENDER_ARMOR_EVENT.register(AvarisGatesClient::renderArmor);

        HudRenderEvents.RENDER_AIR_BUBBLES_EVENT.register(AvarisGatesClient::renderAirBubbles);

        ClientLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }

}
