package com.avaris.averisgates.client;

import net.minecraft.client.option.KeyBinding;

public class AbilityKeyBind {
    private final KeyBinding keyBind;
    public int cooldown = 0;

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
