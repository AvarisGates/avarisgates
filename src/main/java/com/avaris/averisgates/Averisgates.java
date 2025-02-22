package com.avaris.averisgates;

import com.avaris.averisgates.mixin.AccessorRangedAttribute;
import net.fabricmc.api.ModInitializer;
import com.google.common.collect.ImmutableMap;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.Collection;


public class Averisgates implements ModInitializer {

    public static String MOD_ID = "averisgates";
    private static final Double AttrLimit = 1000000000D; //Very important limit (VIL)

    private static final ImmutableMap<Identifier,Double> NEW_DEFAULT_ATTRIBUTES = ImmutableMap.of(
            Identifier.ofVanilla("generic.max_health"), AttrLimit,
            Identifier.ofVanilla("generic.armor"), AttrLimit,
            Identifier.ofVanilla("generic.armor_toughness"), AttrLimit,
            Identifier.ofVanilla("generic.attack_damage"), AttrLimit,
            Identifier.ofVanilla("generic.attack_knockback"),AttrLimit
    );

    @Override
    public void onInitialize() {
        for (Identifier id : Registries.ATTRIBUTE.getIds()) {
            Double new_def_value = NEW_DEFAULT_ATTRIBUTES.get(id);
            if(new_def_value == null){
                continue;
            }
            AccessorRangedAttribute attr = (AccessorRangedAttribute)Registries.ATTRIBUTE.get(id);
            attr.attributefix$setMaxValue(new_def_value);
        }

    }

    private enum Rarity{
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


    private abstract class PlayerClassAbility{
        private PlayerClass.PlayerClassType classType; //This class can access the ability
        private PlayerClassAbilityType abilityType;
        private long minLevel;

        public enum PlayerClassAbilityType{

        }

        public long getMinLevel(){
            return this.minLevel;
        }

        public abstract void trigger();
    }

    private abstract class PlayerClass{
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
}

