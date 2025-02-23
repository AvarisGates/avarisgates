package com.avaris.averisgates.core.player.ability;

import com.avaris.averisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class TeleportAbility extends PlayerClassAbility {

    public TeleportAbility(long ntt, AttachmentType<Long> slot){
        super(ntt,slot);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.Teleport;
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
        HitResult hr = player.raycast(player.getBlockInteractionRange() + 10,0,false);
        if(hr instanceof BlockHitResult blockHitResult){
            BlockPos blockPos = player.getBlockPos().add(0,1,0);
            player.teleportTo(new TeleportTarget(player.getServerWorld(),blockHitResult.getPos(), Vec3d.ZERO,player.getYaw(),player.getPitch(),TeleportTarget.NO_OP));

            ServerWorld serverWorld = player.getServerWorld();
            serverWorld
            .spawnParticles(
                    ParticleTypes.ENCHANT,
                    blockPos.getX(),
                    blockPos.getY() + serverWorld.random.nextDouble(),
                    blockPos.getZ(),
                    60,
                    0.0,
                    0.0,
                    0.0,
                    1.0
            );
            blockPos = player.getBlockPos().add(0,1,0);
            serverWorld
            .spawnParticles(
                    ParticleTypes.COMPOSTER,
                    blockPos.getX(),
                    blockPos.getY() + serverWorld.random.nextDouble(),
                    blockPos.getZ(),
                    100,
                    0.5,
                    0.8,
                    0.5,
                    0.1
            );
            serverWorld.playSound(null,blockPos, SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS);
            serverWorld.playSound(null,blockPos, SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.PLAYERS);
            serverWorld.playSound(null,blockPos, SoundEvents.BLOCK_AZALEA_STEP, SoundCategory.PLAYERS);
        }
        super.trigger(server,player);
    }
}
