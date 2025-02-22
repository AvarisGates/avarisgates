package com.avaris.averisgates.core;

import com.avaris.averisgates.core.network.CastPlayerClassAbilityC2S;
import net.fabricmc.fabric.impl.attachment.sync.c2s.AcceptedAttachmentsPayloadC2S;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;

public abstract class PlayerClassAbility {
    private PlayerClass.PlayerClassType classType; //This class can access the ability
    private PlayerClassAbilityType abilityType;
    private long minLevel;

    public enum PlayerClassAbilityType{
        Swing
        ;

        public static PlayerClassAbilityType fromInt(int i) {
            return Swing;
        }
    }

    public PlayerClassAbilityType getAbilityType(){
        return this.abilityType;
    }

    public long getMinLevel(){
        return this.minLevel;
    }

    public abstract void trigger();
}
