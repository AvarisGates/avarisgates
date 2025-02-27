package com.avaris.avarisgates.core.player.ability;

import com.avaris.avarisgates.core.entity.ability.CleaveEntity;
import com.avaris.avarisgates.core.entity.ModEntities;
import com.avaris.avarisgates.core.player.player_class.PlayerClassType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class CleaveAbility extends PlayerClassAbility{


    public CleaveAbility(AttachedAbility ability) {
        super(ability);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.Cleave;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.Warrior;
    }

    // In minecraft ticks, 1 ticks is 1/20 of a second
    @Override
    public long getBaseCooldown() {
        return 20;
    }

    @Override
    public void trigger(MinecraftServer server, ServerPlayerEntity player) {
        ServerWorld serverWorld = player.getServerWorld();
        CleaveEntity entity = new CleaveEntity(ModEntities.CLEAVE,serverWorld);
        Vec3d rot_vec = player.getRotationVector().multiply(1,0,1).normalize().multiply(1.5);
        Vec3d pos = player.getPos().add(rot_vec);
        entity.setDamage(10);
        entity.setOwnerUuid(player.getUuid());
        entity.setTickLeft(2);
        entity.setPosition(pos);
        serverWorld.spawnEntity(entity);
        serverWorld
        .spawnParticles(
            ParticleTypes.CLOUD,
            pos.getX(),
            pos.getY() + 1,
            pos.getZ(),
            60,
            0.5,
            0.1,
            0.5,
            1.0
        );
        serverWorld.playSound(null,pos.x,pos.y,pos.z, SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS);
        super.trigger(server,player);
    }
}
