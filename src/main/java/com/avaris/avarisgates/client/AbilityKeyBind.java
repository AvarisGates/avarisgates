package com.avaris.avarisgates.client;

import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;
import net.minecraft.client.option.KeyBinding;

public class AbilityKeyBind {
    private final KeyBinding keyBind;
    public int cooldown = 0;

    public PlayerClassAbilityType boundAbility = PlayerClassAbilityType.Swing;

    public AbilityKeyBind(KeyBinding keyBind){
       this.keyBind = keyBind;
    }

    public KeyBinding getKeyBind() {
        return keyBind;
    }

    public int tickCooldown(){
        return --cooldown;
    }

    public boolean wasPressed() {
       return false;
    }
}
