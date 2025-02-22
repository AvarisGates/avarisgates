package com.avaris.averisgates.core;

public abstract class PlayerClassAbility {
    private PlayerClass.PlayerClassType classType; //This class can access the ability
    private PlayerClassAbilityType abilityType;
    private long minLevel;

    public enum PlayerClassAbilityType{

    }

    public long getMinLevel(){
        return this.minLevel;
    }

    public abstract void trigger();
}
