package com.avaris.avarisgates.core.player.ability;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Locale;

public class AbilityRegistrar {

   public static final HashMap<PlayerClassAbilityType,Class<?>> REGISTERED_ABILITIES = new HashMap<>();

    public static <T extends PlayerClassAbility> void register(PlayerClassAbilityType type, Class<T> clazz) {
       REGISTERED_ABILITIES.put(type,clazz);
    }

    public static void init(){
        register(PlayerClassAbilityType.Cleave, CleaveAbility.class);
        register(PlayerClassAbilityType.ShieldBash, ShieldBashAbility.class);
        register(PlayerClassAbilityType.Teleport, TeleportAbility.class);
        register(PlayerClassAbilityType.Whirlwind, WhirlwindAbility.class);
        register(PlayerClassAbilityType.FireBolt, FireBoltAbility.class);
        register(PlayerClassAbilityType.Heal, HealAbility.class);
    }

    public static PlayerClassAbility build(PlayerClassAbilityType type,AttachedAbility ability) {
        try{
            return (PlayerClassAbility)REGISTERED_ABILITIES.get(type).getConstructor(AttachedAbility.class).newInstance(ability);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            //throw new RuntimeException(e);
            e.printStackTrace();
            return null;
        }
    }
}
