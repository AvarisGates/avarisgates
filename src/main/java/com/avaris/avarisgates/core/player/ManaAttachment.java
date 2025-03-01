package com.avaris.avarisgates.core.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;

// Handles mana value and max mana value for a given player
public class ManaAttachment extends PlayerResource{

    // Call this on startup to make fabric realise that the attachments exist
    public static void init(){

    }

    // Init a new instance storing the values
    public ManaAttachment(long value, long maxValue){
        this.value = value;
        this.maxValue = maxValue;
    }

    // Sets the mana as a frabric attachment
    public static void setMana(PlayerEntity player, ManaAttachment mana){
        player.setAttached(getMaxAttachment(), mana.getMaxValue());
        player.setAttached(getValueAttachment(), Math.min(mana.getMaxValue(), mana.getValue()));
    }

    // Get mana attached to a player
    public static ManaAttachment getMana(PlayerEntity player){
        Long value = player.getAttachedOrCreate(getValueAttachment());
        Long maxValue = player.getAttachedOrCreate(getMaxAttachment());
        if(value == null||maxValue == null){
            return null;
        }
        return new ManaAttachment(Math.min(value,maxValue),maxValue);
    }

    // Try to consume a given amount of mana
    // If the player doesn't have enough mana returns false, and doesn't update the players mana
    // Otherwise retrurns true and subtracts the amount from player's mana
    public static boolean consumeMana(PlayerEntity player,long amount){
        ManaAttachment manaAttachment = getMana(player);
        if(manaAttachment == null){
            return false;
        }
        if(manaAttachment.value >= amount){
            manaAttachment.value -= amount;
            setMana(player,manaAttachment);
            return true;
        }
        return false;
    }

    // Returns true if the full mana amount was added
    public static boolean addMana(PlayerEntity player,long amount){
        ManaAttachment manaAttachment = getMana(player);
        if(manaAttachment == null){
            manaAttachment = new ManaAttachment(0,100);
        }
        boolean ret = doesOverflow(manaAttachment,amount);
        manaAttachment.value = Math.min(manaAttachment.maxValue, manaAttachment.value + amount);
        setMana(player,manaAttachment);
        return ret;
    }

    // Check if mana would overflow after adding
    private static boolean doesOverflow(ManaAttachment manaAttachment, long amount){
        return manaAttachment.maxValue >= manaAttachment.value + amount;
    }

    // Handle player mana regeneration
    // Should be called every second (once 20 in-game ticks)
    public static void tickMana(PlayerEntity player){
        ManaAttachment manaAttachment = getMana(player);
        if(manaAttachment == null){
            manaAttachment = new ManaAttachment(0,100);
        }
        if(manaAttachment.value > manaAttachment.maxValue){
            manaAttachment.value = manaAttachment.maxValue;
            setMana(player,manaAttachment);
            return;
        } else if(manaAttachment.value == manaAttachment.maxValue){
            return;
        }
        addMana(player, Attribute.getAttribute(player,AttributeType.Intelligence).getValue());
    }

    private static final AttachmentType<Long> PLAYER_MANA_ATTACHMENT = AttachmentRegistry.create(
            AvarisGates.id("player_mana"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    private static final AttachmentType<Long> PLAYER_MAX_MANA_ATTACHMENT = AttachmentRegistry.create(
            AvarisGates.id("player_max_mana"),
            builder -> builder
                    .initializer(() -> 0L) // start with a default value like hunger
                    .persistent(Codec.LONG) // persist across restarts
                    .syncWith(PacketCodecs.LONG, AttachmentSyncPredicate.all()) // only the player's own client needs the value for rendering
    );

    public static AttachmentType<Long> getValueAttachment() {
        return PLAYER_MANA_ATTACHMENT;
    }

    public static AttachmentType<Long> getMaxAttachment() {
        return PLAYER_MAX_MANA_ATTACHMENT;
    }

    // Initialize mana value for a player if it's not present
    // Then sync it to the client
    // Should be called when a player joins a world
    public static void initForPlayer(ServerPlayerEntity player) {
        // getMana automatically attaches a new ManaAttachment to the player if it isn;t present
        ManaAttachment manaAttachment = ManaAttachment.getMana(player);
        if(manaAttachment == null||manaAttachment.maxValue == 0){
            // Initialize default values
            manaAttachment = new ManaAttachment(100,100);
        }
        //Sync mana with the client
        ManaAttachment.setMana(player,manaAttachment);
    }
}
