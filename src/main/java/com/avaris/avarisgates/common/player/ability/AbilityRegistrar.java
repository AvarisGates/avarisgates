package com.avaris.avarisgates.common.player.ability;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * A utility class used to register and instantiate Abilities from their types.<br>
 *  Use {@link AbilityRegistrar#init()} to register all abilities.<br>
 *  Use {@link AbilityRegistrar#build(PlayerClassAbilityType, AttachedAbility)} to create a new instance of a registered ability.
 */
public class AbilityRegistrar {

   public static final HashMap<PlayerClassAbilityType,Class<?>> REGISTERED_ABILITIES = new HashMap<>();

    public static <T extends PlayerClassAbility> void register(PlayerClassAbilityType type, Class<T> clazz) {
       REGISTERED_ABILITIES.put(type,clazz);
    }

    /**
     * Registers all Abilities, allowing them to be constructed by {@link AbilityRegistrar#build(PlayerClassAbilityType, AttachedAbility)}.<br>
     * Should be called on mod initialization, and always before {@link AbilityRegistrar#build(PlayerClassAbilityType, AttachedAbility)}.
     */
    public static void init(){
        register(PlayerClassAbilityType.Cleave, CleaveAbility.class);
        register(PlayerClassAbilityType.ShieldBash, ShieldBashAbility.class);
        register(PlayerClassAbilityType.Teleport, TeleportAbility.class);
        register(PlayerClassAbilityType.Whirlwind, WhirlwindAbility.class);
        register(PlayerClassAbilityType.FireBolt, FireBoltAbility.class);
        register(PlayerClassAbilityType.Heal, HealAbility.class);
        register(PlayerClassAbilityType.MagicOrb, MagicOrbAbility.class);
    }

    /**
     * Creates a new instance of an ability of the given type.<br>
     * {@link AbilityRegistrar#init()} has to be called before this function.
     * @param type the ability type
     * @param ability the attached ability, representing cooldown
     * @return the new instance or null if the ability type wasn't registered
     */
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
