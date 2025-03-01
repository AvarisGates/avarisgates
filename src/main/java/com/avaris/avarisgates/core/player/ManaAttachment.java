package com.avaris.avarisgates.core.player;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.network.SyncManaS2C;
import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.server.network.ServerPlayerEntity;

public class ManaAttachment extends PlayerResource{
    public ManaAttachment(long value, long maxValue){
        this.value = value;
        this.maxValue = maxValue;
    }

    public static void setMana(ServerPlayerEntity player, ManaAttachment mana){
        setMana(player,mana,false);
    }

    public static void setMana(ServerPlayerEntity player, ManaAttachment mana,boolean sendToClient){
        player.setAttached(getMaxAttachment(), mana.getMaxValue());
        player.setAttached(getValueAttachment(), Math.min(mana.getMaxValue(), mana.getValue()));
        if(sendToClient){
            ServerPlayNetworking.send(player,new SyncManaS2C(mana));
        }
    }

    public static ManaAttachment getMana(ServerPlayerEntity player){
        Long value = player.getAttached(getValueAttachment());
        Long maxValue = player.getAttached(getMaxAttachment());
        if(value == null||maxValue == null){
            return null;
        }
        return new ManaAttachment(Math.min(value,maxValue),maxValue);
    }

    public static boolean consumeMana(ServerPlayerEntity player,long amount){
        ManaAttachment manaAttachment = getMana(player);
        if(manaAttachment == null){
            return false;
        }
        if(manaAttachment.value >= amount){
            manaAttachment.value -= amount;
            setMana(player,manaAttachment,true);
            return true;
        }
        return false;
    }

    // Returns true if the full mana amount was added
    public static boolean addMana(ServerPlayerEntity player,long amount){
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

    public static void tickMana(ServerPlayerEntity player){
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

    public static long tickClientMana(long mana, long maxMana) {
        return Math.min(maxMana, mana + Attribute.getAttribute(MinecraftClient.getInstance().player, AttributeType.Intelligence).getValue());
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

    public static void initForPlayer(ServerPlayerEntity player) {
        ManaAttachment manaAttachment = ManaAttachment.getMana(player);
        if(manaAttachment == null||manaAttachment.maxValue == 0){
            manaAttachment = new ManaAttachment(100,100);
        }
        //Sync mana with the client
        ManaAttachment.setMana(player,manaAttachment,true);
    }
}
