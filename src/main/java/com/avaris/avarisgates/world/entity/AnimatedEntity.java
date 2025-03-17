package com.avaris.avarisgates.world.entity;

import com.avaris.avarisgates.AvarisGates;
import com.nimbusds.jose.util.ArrayUtils;
import net.minecraft.entity.Entity;
import net.minecraft.network.FriendlyByteBuf;

public interface AnimatedEntity extends AdditionalSpawnDataEntity {
    EntityAnimation getAnimation();
    void setAnimation(EntityAnimation var1);
    EntityAnimation[] getAnimations();
    int getAnimationTick();
    void setAnimationTick(int var1);

    default boolean isAnimationPlaying(){
        return this.getAnimation() != EntityAnimation.NONE;
    }

    @Override
    default void writeAdditionalAddEntityData(FriendlyByteBuf buffer){
        buffer.writeInt(ArrayUtils.indexOf(this.getAnimations(), this.getAnimation()));
        buffer.writeInt(this.getAnimationTick());
    }

    @Override
    default void readAdditionalAddEntityData(FriendlyByteBuf additionalData){
        int animationId = additionalData.readInt();
        this.setAnimation(animationId < 0 ? EntityAnimation.NONE : this.getAnimations()[animationId]);
        this.setAnimationTick(additionalData.readInt());
    }

    static <T extends Entity & AnimatedEntity> void sendAnimationPacket(T entity, EntityAnimation animation){
        if(!entity.level().isClientSide){
            entity.setAnimation(animation);
            entity.setAnimationTick(0);
            //TODO: GOTTA FIX THIS PART
            PlayerSet playerSet = PlayerSet.nearEntity(entity);
            AvarisGates.NETWORK.sendMessage(playerSet, new S2CAnimationMessage(entity.getId(),
                    ArrayUtils.indexOf(entity.getAnimations(), animation)
            ).toClientboundMessage());
        }
    }
}
