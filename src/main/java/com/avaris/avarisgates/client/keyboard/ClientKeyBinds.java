package com.avaris.avarisgates.client.keyboard;

import com.avaris.avarisgates.common.network.CastPlayerClassAbilityC2S;
import com.avaris.avarisgates.common.network.ChangeAbilityS2C;
import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientKeyBinds {
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

    private static void checkKeyBind(MinecraftClient client, AbilityKeyBind keyBind){
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

    public static void init(){
        // These are just initial values, the server syncs them on join anyway
        ABILITY_0_KEY_BIND.boundAbility = PlayerClassAbilityType.Whirlwind;
        ABILITY_1_KEY_BIND.boundAbility = PlayerClassAbilityType.Cleave;
        ABILITY_2_KEY_BIND.boundAbility = PlayerClassAbilityType.Teleport;
    }

    public static void tick(MinecraftClient client){
        checkKeyBind(client,ABILITY_0_KEY_BIND);
        checkKeyBind(client,ABILITY_1_KEY_BIND);
        checkKeyBind(client,ABILITY_2_KEY_BIND);
    }

    public static void receiveChangeAbility(ChangeAbilityS2C packet, ClientPlayNetworking.Context context) {
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
