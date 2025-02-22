package com.avaris.averisgates.client;

import com.avaris.averisgates.core.PlayerClassAbility;
import net.minecraft.client.option.KeyBinding;

public class AbilityKeyBind {
    private final KeyBinding keyBind;
    public int cooldown = 0;

    public PlayerClassAbility.PlayerClassAbilityType boundAbility = PlayerClassAbility.PlayerClassAbilityType.Swing;

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
