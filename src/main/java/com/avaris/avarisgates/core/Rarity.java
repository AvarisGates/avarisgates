package com.avaris.avarisgates.core;

public enum Rarity {
    COMMON,
    UNCOMMON,
    RARE,
    EPIC,
    LEGENDARY;

    public static Rarity fromInt(int i){
        return switch (i) {
            case 1 -> UNCOMMON;
            case 2 -> RARE;
            case 3 -> EPIC;
            case 4 -> LEGENDARY;
            default -> COMMON;
        };
    }

    public Rarity next(){
        return fromInt(this.ordinal()+1);
    }
}
