package com.avaris.avarisgates.core.player.player_class;

import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;

import java.util.Collection;
import java.util.List;

public class PriestPlayerClass extends PlayerClass {

    public PriestPlayerClass(long experience) { super(PlayerClassType.Priest, experience);
    }

    @Override
    public Collection<PlayerClassAbilityType> getValidAbilities() {
        return List.of();
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
