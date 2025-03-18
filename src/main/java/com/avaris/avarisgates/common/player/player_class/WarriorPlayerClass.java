package com.avaris.avarisgates.common.player.player_class;

import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;

import java.util.Collection;
import java.util.List;

public class WarriorPlayerClass extends PlayerClass {

    public WarriorPlayerClass(long experience) {
        super(PlayerClassType.Warrior, experience);
    }

    @Override
    public Collection<PlayerClassAbilityType> getValidAbilities() {
        return List.of(PlayerClassAbilityType.Cleave);
    }

    @Override
    public Collection<PlayerClassAbilityType> getLearnedAbilities() {
        return List.of();
    }

    @Override
    public Collection<PlayerClassAbilityType> getActiveAbilities() {
        return List.of();
    }
}
