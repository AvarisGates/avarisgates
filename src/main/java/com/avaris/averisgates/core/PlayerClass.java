package com.avaris.averisgates.core;

import com.avaris.averisgates.Averisgates;

import java.util.Collection;

public abstract class PlayerClass {
    public enum PlayerClassType{
        //AVAILABLE IN BETA
        Warrior,
        Archer,
        Mage,
        Priest,

        //In Release
        Rogue,
        Tamer,
        Summoner,
        Necromancer
    }

    private PlayerClassType type;

    public static long MAX_CLASS_LEVEL = 100;

    private long experience; //Class experience
    private long level; //Class levels

    public PlayerClass(PlayerClassType type,long experience){
        this.type = type;
        this.experience = experience;
        this.level = calculateLevel(level);
    }

    //TODO:
    private long calculateLevel(long experience){
        throw new UnsupportedOperationException("Feature incomplete. Contact assistance.");
        // return 0;
    }

    public abstract Collection<PlayerClassAbility> getValidAbilities();

    public abstract Collection<PlayerClassAbility> getLearnedAbilities();

    public abstract Collection<PlayerClassAbility> getActiveAbilities();


}
