package com.avaris.avarisgates.common.entity.ability;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class MagicOrbEntity extends ProjectileEntity {

    private static final int MAX_AGE = 600;// In ticks 600 ticks = 30 seconds

    public MagicOrbEntity(EntityType<? extends ProjectileEntity> entityType, World world) {
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
                                    ParticleTypes.END_ROD,
                                    pos.getX(),
                                    pos.getY() + 1,
                                    pos.getZ(),
                                    3,
                                    0.5,
                                    0.1,
                                    0.5,
                                    0.1
                            );
                    world
                            .spawnParticles(
                                    ParticleTypes.SPIT,
                                    pos.getX(),
                                    pos.getY() + 1,
                                    pos.getZ(),
                                    3,
                                    0.1,
                                    0.1,
                                    0.1,
                                    0.1
                            );
                    world.playSound(null, pos.x, pos.y + 1,pos.z ,SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.PLAYERS);
                    this.discard();
                }
            }
        }
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public void tick() {
        if(this.age > MAX_AGE){
            this.discard();
            return;
        }


        // Borrowed from EggEntity
        HitResult hitResult = ProjectileUtil.getCollision(this, this::canHit);
        Vec3d vec3d;
        if (hitResult.getType() != HitResult.Type.MISS) {
            vec3d = hitResult.getPos();
        } else {
            vec3d = this.getPos().add(this.getVelocity());
        }

        this.setPosition(vec3d);
        this.updateRotation();
        this.tickBlockCollision();
        super.tick();
        if (hitResult.getType() != HitResult.Type.MISS && this.isAlive()) {
            this.hitOrDeflect(hitResult);
        }
    }
}
