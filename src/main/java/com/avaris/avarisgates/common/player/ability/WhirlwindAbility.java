package com.avaris.avarisgates.common.player.ability;

import com.avaris.avarisgates.common.entity.ModEntities;
import com.avaris.avarisgates.common.entity.ability.WhirlwindEntity;
import com.avaris.avarisgates.common.player.player_class.PlayerClassType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WhirlwindAbility extends PlayerClassAbility{

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public WhirlwindAbility(AttachedAbility attachedAbility) {
        super(attachedAbility);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.Whirlwind;
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
    public boolean trigger(MinecraftServer server, ServerPlayerEntity player) {
        if(!super.trigger(server,player)){
            return false;
        }
        for (int i = 0; i < 3; i++) {
            scheduler.schedule(() -> {
                ServerWorld serverWorld = player.getServerWorld();
                WhirlwindEntity entity = new WhirlwindEntity(ModEntities.WHIRLWIND,serverWorld);
                Vec3d pos = player.getPos();
                entity.setDamage(10);
                entity.setOwnerUuid(player.getUuid());
                entity.setTickLeft(2);
                entity.setBoundingBox(entity.getBoundingBox().expand(8.0, 1.0, 8.0));
                entity.setPosition(pos);
                serverWorld.spawnEntity(entity);
                serverWorld
                        .spawnParticles(
                                ParticleTypes.CLOUD,
                                pos.getX(),
                                pos.getY() + 1,
                                pos.getZ(),
                                1,
                                0.0,
                                0.0,
                                0.0,
                                0.0
                        );
                serverWorld.playSound(null,pos.x,pos.y,pos.z, SoundEvents.BLOCK_BELL_USE, SoundCategory.PLAYERS);
            }, i, TimeUnit.SECONDS);
        }
        return true;
    }
}
