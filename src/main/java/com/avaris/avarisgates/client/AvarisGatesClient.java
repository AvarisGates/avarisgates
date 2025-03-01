package com.avaris.avarisgates.client;

import com.avaris.avarisgates.core.currency.CurrencyAttachment;
import com.avaris.avarisgates.core.entity.ModEntities;
import com.avaris.avarisgates.core.entity.ability.renderer.CleaveEntityRenderer;
import com.avaris.avarisgates.core.entity.ability.renderer.FireBoltEntityRenderer;
import com.avaris.avarisgates.core.entity.ability.renderer.WhirlwindEntityRenderer;
import com.avaris.avarisgates.core.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.core.network.ChangeAbilityS2C;
import com.avaris.avarisgates.core.player.ManaAttachment;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

public class AvarisGatesClient implements ClientModInitializer {

        private static final AbilityKeyBind ABILITY_0_KEY_BIND = new AbilityKeyBind(
                KeyBindingHelper.registerKeyBinding(new KeyBinding(
    "key.averisgates.ability_0", // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_R, // The keycode of the key
                "category.averisgates.abilities" // The translation key of the keybinding's category.
                )));

    private static final AbilityKeyBind ABILITY_1_KEY_BIND = new AbilityKeyBind(
            KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.averisgates.ability_1", // The translation key of the keybinding's name
                    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                    GLFW.GLFW_KEY_X, // The keycode of the key
                    "category.averisgates.abilities" // The translation key of the keybinding's category.
            )));

    private static final AbilityKeyBind ABILITY_2_KEY_BIND = new AbilityKeyBind(
            KeyBindingHelper.registerKeyBinding(new KeyBinding("key.averisgates.ability_2", // The translation key of the keybinding's name
                    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                    GLFW.GLFW_KEY_C, // The keycode of the key
                    "category.averisgates.abilities" // The translation key of the keybinding's category.
            )));

    public static long getLevel() {
       return 300;
    }

    public static long getAttributeValue(AttributeType type) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if(player == null){
            return 0;
        }
        return Objects.requireNonNullElse(Attribute.getAttribute(player, type).getValue(),0L);
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

    private void checkKeyBind(MinecraftClient client,AbilityKeyBind keyBind){
        int cooldown = keyBind.tickCooldown();
        boolean pressed = keyBind.getKeyBind().isPressed();
        if(cooldown >= 0 && pressed){
            client.player.sendMessage(Text.literal("Ability on cooldown for ").append(Text.literal(String.valueOf(keyBind.cooldown))),true);
        }else if(pressed){
            keyBind.cooldown = 20;
            client.player.sendMessage(Text.literal("Client sent ability: ").append(keyBind.boundAbility.toString()), true);
            ClientPlayNetworking.send(new CastPlayerClassAbilityC2S(keyBind.boundAbility));
        }
    }

    @Override
    public void onInitializeClient() {
        ABILITY_0_KEY_BIND.boundAbility = PlayerClassAbilityType.Whirlwind;
        ABILITY_1_KEY_BIND.boundAbility = PlayerClassAbilityType.Cleave;
        ABILITY_2_KEY_BIND.boundAbility = PlayerClassAbilityType.Teleport;
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            checkKeyBind(client,ABILITY_0_KEY_BIND);
            checkKeyBind(client,ABILITY_1_KEY_BIND);
            checkKeyBind(client,ABILITY_2_KEY_BIND);
        });


        /*
         * Registers our Cube Entity's renderer, which provides a model and texture for the entity.
         *
         * Entity Renderers can also manipulate the model before it renders based on entity context (EndermanEntityRenderer#render).
         */

        // In 1.17, use EntityRendererRegistry.register (seen below) instead of EntityRendererRegistry.INSTANCE.register (seen above)
        EntityRendererRegistry.register(ModEntities.CLEAVE, CleaveEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.WHIRLWIND, WhirlwindEntityRenderer::new);
        EntityRendererRegistry.register(ModEntities.FIREBOLT, FireBoltEntityRenderer::new);

        ClientPlayNetworking.registerGlobalReceiver(ChangeAbilityS2C.ID, this::receiveChangeAbility);
    }

    private void receiveChangeAbility(ChangeAbilityS2C packet, ClientPlayNetworking.Context context) {
        if(packet.slot() == 0){
            ABILITY_0_KEY_BIND.boundAbility = packet.ability();
        }

        if(packet.slot() == 1){
            ABILITY_1_KEY_BIND.boundAbility = packet.ability();
        }
        if(packet.slot() == 2){
            ABILITY_2_KEY_BIND.boundAbility = packet.ability();
        }
    }
}
