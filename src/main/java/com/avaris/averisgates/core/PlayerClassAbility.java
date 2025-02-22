package com.avaris.averisgates.core;

public abstract class PlayerClassAbility {
    private PlayerClass.PlayerClassType classType; //This class can access the ability
    private PlayerClassAbilityType abilityType;
    private long minLevel;

    public enum PlayerClassAbilityType{
        Swing,
        Cleave
        ;

        public static PlayerClassAbilityType fromInt(int i) {
            switch (i) {
                case 0 -> {
                    return Swing;
                }
                case 1 -> {
                    return Cleave;
                }
                default -> throw new IllegalStateException("Unexpected value: " + i);
            }
        }
    }

    public PlayerClassAbilityType getAbilityType(){
        return this.abilityType;
    }

    public long getMinLevel(){
        return this.minLevel;
    }

    public abstract void trigger();
}
