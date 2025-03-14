package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class HealAbility extends PlayerClassAbility{
    public HealAbility(AttachedAbility ability) {
        super(ability);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.Heal;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.Priest;
    }

    @Override
    public long getBaseCooldown() {
        return 20;
    }

    @Override
    public boolean trigger(MinecraftServer server, ServerPlayerEntity player) {
        if(!super.trigger(server, player)){
            return false;
        }
        HitResult hitResult = AbilityUtil.findCrosshairTarget(player,0,player.getEntityInteractionRange(),0);
        if(hitResult instanceof EntityHitResult entityHitResult){
            if(entityHitResult.getEntity() instanceof LivingEntity entity){
               entity.heal(Attribute.getAttributeWithEffects(player, AttributeType.Faith).getValue());
                if(entity.getWorld() instanceof ServerWorld serverWorld){
                    Vec3d pos = entity.getPos();
                    serverWorld
                            .spawnParticles(
                                    ParticleTypes.GLOW,
                                    pos.getX(),
                                    pos.getY() + entity.getEyeHeight(entity.getPose()),
                                    pos.getZ(),
                                    30,
                                    0.7,
                                    1,
                                    0.7,
                                    1.2
                            );
                    serverWorld
                            .spawnParticles(
                                    ParticleTypes.CRIMSON_SPORE,
                                    pos.getX(),
                                    pos.getY(),
                                    pos.getZ(),
                                    30,
                                    0.1,
                                    0.0,
                                    0.1,
                                    1.2
                            );
                    serverWorld .playSound(null,
                                    pos.x,
                                    pos.y + 1,
                                    pos.z ,
                                    SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE,
                                    SoundCategory.PLAYERS);
                }
            }
        }
        return true;
    }
}
