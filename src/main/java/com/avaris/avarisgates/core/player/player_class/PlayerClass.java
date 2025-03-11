package com.avaris.avarisgates.core.player.player_class;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.ability.PlayerClassAbilityType;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class PlayerClass {
    public static final AttachmentType<PlayerClassType> PLAYER_CLASS_TYPE_ATTACHMENT = AttachmentRegistry.create(
            AvarisGates.id("player_class_type"),
            builder -> builder
                .initializer(() -> PlayerClassType.Beginner) // start with a default value like hunger
                .persistent(PlayerClassType.CODEC) // persist across restarts
                .copyOnDeath()
                .syncWith(PlayerClassType.PACKET_CODEC, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    private static final Map<PlayerClassType, String> EXPERIENCE_IDS = new HashMap<>();

    static {
        //We initialize the map with experience IDs for each class type
        EXPERIENCE_IDS.put(PlayerClassType.Beginner, "player_experience_all");
        EXPERIENCE_IDS.put(PlayerClassType.Warrior, "player_experience_warrior");
        //EXAMPLE OF CLASSES WITH THE SAME LINE:
        //EXPERIENCE_IDS.put(PlayerClassType.GreatWarrior, "player_experience_warrior");
        EXPERIENCE_IDS.put(PlayerClassType.Mage, "player_experience_mage");
        EXPERIENCE_IDS.put(PlayerClassType.Archer, "player_experience_archer");
        EXPERIENCE_IDS.put(PlayerClassType.Priest, "player_experience_priest");
        //EXPERIENCE_IDS.put(PlayerClassType.Rogue, "player_experience_rogue");
        //ADD REMAINING CLASSES LIKE THE ONES ABOVE
    }

    public static final AttachmentType<Long> PLAYER_EXPERIENCE_ATTACHMENT = AttachmentRegistry.create(
            AvarisGates.id(getExperienceId(PlayerClassType.Beginner)),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .copyOnDeath()
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    // Call this on startup to make fabric realise that the attachments exist
    public static void init(){

    }

    private final PlayerClassType type;

    private long experience; //Class experience
    private long level; //Class levels

    public PlayerClass(PlayerClassType type,long experience){
        this.type = type;
        this.experience = experience;
        this.level = calculateLevel(experience);
    }
    
    private long calculateLevel(long experience) {
        long baseXp = 100;  // Base XP requirement for level 1
        double multiplier = 1.0;

        int level = 0;
        long totalXp = 0;

        while (totalXp <= experience) {
            level++;
            totalXp += (long) (baseXp * Math.pow(level, 1.005) * multiplier);
            totalXp = (totalXp / 100) * 100;  // Round down to the nearest multiple of 100
        }

        return level - 1;
    }

    private static String getExperienceId(PlayerClassType type69){
        //TODO: ADD A WORKING RETRIEVING EXPERIENCE ID FUNCTION, idk if this works
        AvarisGates.LOGGER.info("Print type:{}",type69.toString()); //DEBUGGING
        return EXPERIENCE_IDS.getOrDefault(type69, "player_experience_all");
    }

    public abstract Collection<PlayerClassAbilityType> getValidAbilities();

    public abstract Collection<PlayerClassAbilityType> getLearnedAbilities();

    public abstract Collection<PlayerClassAbilityType> getActiveAbilities();


}
