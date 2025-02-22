package com.avaris.averisgates.client;

import com.avaris.averisgates.core.PlayerClassAbility;
import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class AverisgatesClient implements ClientModInitializer {

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
            KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.averisgates.ability_2", // The translation key of the keybinding's name
                    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                    GLFW.GLFW_KEY_C, // The keycode of the key
                    "category.averisgates.abilities" // The translation key of the keybinding's category.
            )));

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            boolean wasPressed = ABILITY_0_KEY_BIND.getKeyBind().wasPressed();
            int cooldown = ABILITY_0_KEY_BIND.tickCooldown();
            if(cooldown >= 0 && wasPressed){
                client.player.sendMessage(Text.literal("Ability on cooldown for ").append(Text.literal(String.valueOf(ABILITY_0_KEY_BIND.cooldown))),true);
            }else if(wasPressed){
                ABILITY_0_KEY_BIND.cooldown = 20 * 5;
                client.player.sendMessage(Text.literal("Client sent ability one"), false);
                ClientPlayNetworking.send(new CastPlayerClassAbilityC2S(PlayerClassAbility.PlayerClassAbilityType.Swing));
            }
        });
    }
}
