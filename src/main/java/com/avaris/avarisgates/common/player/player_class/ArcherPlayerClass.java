package com.avaris.avarisgates.common.player.player_class;

import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;

import java.util.Collection;
import java.util.List;

public class ArcherPlayerClass extends PlayerClass {

    public ArcherPlayerClass(long experience) { super(PlayerClassType.Archer, experience);
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
