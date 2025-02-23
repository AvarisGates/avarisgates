package com.avaris.avarisgates.core.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

public class WhirlwindEntity extends Entity {
    private UUID ownerUUID;
    private float damage;


    private int tickLeft; //Ticks left until despawn

    public WhirlwindEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        return false;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.ownerUUID = nbt.getUuid("owner");
        this.damage = nbt.getFloat("damage");
        this.tickLeft = nbt.getInt("ticks_left");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putUuid("owner",this.ownerUUID);
        nbt.putFloat("damage",this.damage);
        nbt.putInt("ticks_left",this.tickLeft);
    }

    @Override
    public void tick() {
        if(this.tickLeft-- < 0){
            this.remove(RemovalReason.DISCARDED);
            return;
        }
        super.tick();
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

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUUID = ownerUuid;
    }
    public int getTickLeft() {
        return tickLeft;
    }

    public void setTickLeft(int tickLeft) {
        this.tickLeft = tickLeft;
    }
}
