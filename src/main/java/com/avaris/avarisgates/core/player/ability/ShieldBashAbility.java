package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.AvarisGates;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.world.BlockView;

public class ShieldBashAbility extends PlayerClassAbility<ShieldBashAbility> {
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
    public void trigger(MinecraftServer server, ServerPlayerEntity player) {
        super.trigger(server, player);
        for(Hand hand : Hand.values()){
            ItemStack stack = player.getStackInHand(hand);
            if(stack.getItem() instanceof ShieldItem shieldItem){
                HitResult hitResult = findCrosshairTarget(player,0,player.getEntityInteractionRange(),0);
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
    }


    private HitResult findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
        double d = Math.max(blockInteractionRange, entityInteractionRange);
        double e = MathHelper.square(d);
        Vec3d vec3d = camera.getCameraPosVec(tickDelta);
        HitResult hitResult = camera.raycast(d, tickDelta, false);
        double f = hitResult.getPos().squaredDistanceTo(vec3d);
        if (hitResult.getType() != HitResult.Type.MISS) {
            e = f;
            d = Math.sqrt(f);
        }

        Vec3d vec3d2 = camera.getRotationVec(tickDelta);
        Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
        float g = 1.0F;
        Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
        EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, EntityPredicates.CAN_HIT, e);
        return entityHitResult != null && entityHitResult.getPos().squaredDistanceTo(vec3d) < f
                ? ensureTargetInRange(entityHitResult, vec3d, entityInteractionRange)
                : ensureTargetInRange(hitResult, vec3d, blockInteractionRange);
    }

    private static HitResult ensureTargetInRange(HitResult hitResult, Vec3d cameraPos, double interactionRange) {
        Vec3d vec3d = hitResult.getPos();
        if (!vec3d.isInRange(cameraPos, interactionRange)) {
            Vec3d vec3d2 = hitResult.getPos();
            Direction direction = Direction.getFacing(vec3d2.x - cameraPos.x, vec3d2.y - cameraPos.y, vec3d2.z - cameraPos.z);
            return BlockHitResult.createMissed(vec3d2, direction, BlockPos.ofFloored(vec3d2));
        } else {
            return hitResult;
        }
    }
}
