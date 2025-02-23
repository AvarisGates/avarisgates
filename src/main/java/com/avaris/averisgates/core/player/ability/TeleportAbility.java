package com.avaris.averisgates.core.player.ability;

import com.avaris.averisgates.core.player.player_class.PlayerClassType;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class TeleportAbility extends PlayerClassAbility {

    public TeleportAbility(){
       this.nextTriggerTime = 0;
    }

    public TeleportAbility(long ntt, AttachmentType<Long> slot){
        this.nextTriggerTime = ntt;
        this.slot = slot;
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
            player.teleportTo(new TeleportTarget(player.getServerWorld(),blockHitResult.getPos(), Vec3d.ZERO,player.getYaw(),player.getPitch(),TeleportTarget.NO_OP));
        }
        super.trigger(server,player);
    }
}
