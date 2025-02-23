package com.avaris.avarisgates.core.player.player_class;

import com.avaris.avarisgates.core.player.ability.PlayerClassAbility;

import java.util.Collection;
import java.util.List;

public class WarriorPlayerClass extends PlayerClass {



    public WarriorPlayerClass(long experience) {
        super(PlayerClassType.Warrior, experience);
    }

    @Override
    public Collection<PlayerClassAbility> getValidAbilities() {
        return List.of();
    }

    @Override
    public Collection<PlayerClassAbility> getLearnedAbilities() {
        return List.of();
    }

    @Override
    public Collection<PlayerClassAbility> getActiveAbilities() {
        return List.of();
    }
}
