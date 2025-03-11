package com.avaris.avarisgates.core.entity.ability;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ExplosiveProjectileEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class FireBoltEntity extends ExplosiveProjectileEntity {

    public FireBoltEntity(EntityType<? extends ExplosiveProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if(this.getWorld() instanceof ServerWorld world){
            if (hitResult.getType() != HitResult.Type.ENTITY || !this.isOwner(((EntityHitResult)hitResult).getEntity())) {
                if (!this.getWorld().isClient) {
                    List<LivingEntity> list = world.getNonSpectatingEntities(LivingEntity.class, this.getBoundingBox().expand(4.0, 2.0, 4.0));
                    if (!list.isEmpty()) {
                        for (LivingEntity livingEntity : list) {
                            if(livingEntity.equals(this.getOwner())){
                                continue;
                            }
                            livingEntity.damage(world,this.getDamageSources().playerAttack(((PlayerEntity) this.getOwner())),10);
                        }
                    }
                    Vec3d pos = this.getPos();
                    world
                            .spawnParticles(
                                    ParticleTypes.FIREWORK,
                                    pos.getX(),
                                    pos.getY() + 1,
                                    pos.getZ(),
                                    20,
                                    0.5,
                                    0.1,
                                    0.5,
                                    1.0
                            );
                    world
                            .spawnParticles(
                                    ParticleTypes.LAVA,
                                    pos.getX(),
                                    pos.getY() + 1,
                                    pos.getZ(),
                                    30,
                                    0.5,
                                    0.1,
                                    0.5,
                                    3.0
                            );
                    world.playSound(null, pos.x, pos.y + 1,pos.z ,SoundEvents.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.PLAYERS);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected boolean isBurning() {
        return false;
    }
}
