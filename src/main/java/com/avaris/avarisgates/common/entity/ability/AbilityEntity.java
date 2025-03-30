package com.avaris.avarisgates.common.entity.ability;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Uuids;
import net.minecraft.world.World;

import java.util.UUID;

public class AbilityEntity extends Entity{
    protected UUID ownerUUID;
    protected float damage;
    protected int tickLeft; //Ticks left until despawn

    public AbilityEntity(EntityType<?> type, World world) {
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
        this.ownerUUID = nbt.get("owner", Uuids.CODEC).orElse(null);
        this.damage = nbt.getFloat("damage").orElse(0.f);
        this.tickLeft = nbt.getInt("ticks_left").orElse(0);
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("owner",Uuids.CODEC,this.ownerUUID);
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
