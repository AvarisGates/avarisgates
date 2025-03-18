package com.avaris.avarisgates.common.player.player_class;

import com.avaris.avarisgates.common.player.ability.PlayerClassAbilityType;

import java.util.Collection;
import java.util.List;

public class MagePlayerClass extends PlayerClass {

    public MagePlayerClass(long experience) { super(PlayerClassType.Mage, experience);
    }

    @Override
    public Collection<PlayerClassAbilityType> getValidAbilities() {
        return List.of(PlayerClassAbilityType.FireBolt);
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
