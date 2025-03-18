package com.avaris.avarisgates.common.entity.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;

public class WhirlwindEntity extends AbilityEntity {

    public WhirlwindEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        super.tick();
        if(this.isRemoved()){
            return;
        }
        if(this.getWorld() instanceof ServerWorld world){
            List<Entity> collisions = world.getOtherEntities(this,this.getBoundingBox());
            for(Entity collision : collisions){
                if(collision.getUuid().equals(this.ownerUUID)){
                    continue;
                }
                collision.damage(world,this.getDamageSources().generic(),this.damage);
            }
        }
    }
}
