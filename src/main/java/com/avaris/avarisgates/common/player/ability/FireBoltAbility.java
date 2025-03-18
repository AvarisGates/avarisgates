package com.avaris.avarisgates.common.player.ability;

import com.avaris.avarisgates.common.entity.ModEntities;
import com.avaris.avarisgates.common.entity.ability.FireBoltEntity;
import com.avaris.avarisgates.common.player.player_class.PlayerClassType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public class FireBoltAbility extends PlayerClassAbility{

    public FireBoltAbility(AttachedAbility ability) {
        super(ability);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.FireBolt;
    }

    @Override
    public PlayerClassType getClassType() {
        return PlayerClassType.Mage;
    }

    @Override
    public long getBaseCooldown() {
        return 10;
    }

    @Override
    public boolean trigger(MinecraftServer server, ServerPlayerEntity player) {
        if(!super.trigger(server, player)){
            return false;
        }
        FireBoltEntity entity = new FireBoltEntity(ModEntities.FIREBOLT,player.getServerWorld());
        entity.setOwner(player);
        entity.setPosition(player.getPos().add(0,player.getEyeHeight(player.getPose()) / 2,0));
        player.getServerWorld().spawnEntity(entity);
        entity.setVelocity(player,player.getPitch(),player.getYaw(),0,0.001f,0);
        return true;
    }
}
