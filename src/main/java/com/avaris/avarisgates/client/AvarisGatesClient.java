package com.avaris.avarisgates.client;

import com.avaris.avarisgates.client.keyboard.ClientKeyBinds;
import com.avaris.avarisgates.client.render.handler.GuiRenderEventsHandler;
import com.avaris.avarisgates.client.render.handler.HudRenderEventsHandler;
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
import com.avaris.towncrier.client.api.v1.impl.ClientPlayerEvents;
import com.avaris.towncrier.client.api.v1.impl.InteractionManagerEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

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


    public static Text getChatChannelName() {
        return Text.literal("GLOBAL");
    }

    public static Text getChatChannelDescription() {
        return Text.literal("Current Channel");
    }

    private static boolean hasExperienceBar(ClientPlayerInteractionManager manager) {
        return MinecraftClient.isHudEnabled();
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
        InteractionManagerEvents.HAS_EXPERIENCE_BAR_EVENT.register(AvarisGatesClient::hasExperienceBar);

        GuiRenderEventsHandler.init();
        HudRenderEventsHandler.init();
        
        ClientLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }

}
