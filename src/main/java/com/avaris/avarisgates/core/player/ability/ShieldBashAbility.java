package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;

public class ShieldBashAbility extends PlayerClassAbility{
    public ShieldBashAbility(AttachedAbility ability) {
        super(ability);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.ShieldBash;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.Warrior;
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
        for(Hand hand : Hand.values()){
            ItemStack stack = player.getStackInHand(hand);
            if(stack.getItem() instanceof ShieldItem shieldItem){
                HitResult hitResult = AbilityUtil.findCrosshairTarget(player,0,player.getEntityInteractionRange(),0);
                AvarisGates.LOGGER.info("{}",hitResult.getType());
               if(hitResult instanceof EntityHitResult entityHitResult){
                    Entity entity = entityHitResult.getEntity();
                    ServerWorld serverWorld = player.getServerWorld();
                    entity.damage(serverWorld, player.getDamageSources().playerAttack(player),10);
                    stack.damage(1,player);
                    player.swingHand(hand,true);
                    Vec3d pos = entity.getPos();
                    serverWorld
                    .spawnParticles(
                            ParticleTypes.ENCHANTED_HIT,
                            pos.x,
                            pos.y + entity.getEyeHeight(entity.getPose()) / 2,
                            pos.z,
                            60,
                            0.0,
                            0.0,
                            0.0,
                            1.0
                    );
                    serverWorld.playSound(null,
                            pos.x,
                            pos.y + entity.getEyeHeight(entity.getPose()) / 2,
                            pos.z,
                            SoundEvents.ITEM_SHIELD_BLOCK,
                            SoundCategory.PLAYERS,0.5f,1.f);
                   serverWorld.playSound(null,
                           pos.x,
                           pos.y + entity.getEyeHeight(entity.getPose()) / 2,
                           pos.z,
                           SoundEvents.BLOCK_ANVIL_FALL,
                           SoundCategory.PLAYERS,0.5f,1.f);
                   serverWorld.playSound(null,
                           pos.x,
                           pos.y + entity.getEyeHeight(entity.getPose()) / 2,
                           pos.z,
                           SoundEvents.BLOCK_BASALT_STEP,
                           SoundCategory.PLAYERS,0.4f,1.f);
                   serverWorld.playSound(null,
                           pos.x,
                           pos.y + entity.getEyeHeight(entity.getPose()) / 2,
                           pos.z,
                           SoundEvents.BLOCK_CHAIN_FALL,
                           SoundCategory.PLAYERS,0.2f,0.05f);
                }
            break;
            }
        }
        return true;
    }


}
