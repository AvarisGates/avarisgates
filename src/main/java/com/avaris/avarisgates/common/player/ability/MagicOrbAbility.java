package com.avaris.avarisgates.common.player.ability;

import com.avaris.avarisgates.common.entity.ModEntities;
import com.avaris.avarisgates.common.entity.ability.MagicOrbEntity;
import com.avaris.avarisgates.common.player.player_class.PlayerClassType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;

public class MagicOrbAbility extends PlayerClassAbility{

    public MagicOrbAbility(AttachedAbility ability) {
        super(ability);
    }

    @Override
    public PlayerClassAbilityType getAbilityType() {
        return PlayerClassAbilityType.MagicOrb;
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
    public long getBaseManaCost() {
        return 0;
    }

    @Override
    public boolean trigger(MinecraftServer server, ServerPlayerEntity player) {
        if(!super.trigger(server, player)){
            return false;
        }
        MagicOrbEntity entity = new MagicOrbEntity(ModEntities.MAGICORB,player.getServerWorld());
        entity.setOwner(player);
        entity.setVelocity(player,player.getPitch(),player.getYaw(),0,2.5f,0);
        entity.setPosition(player.getPos().add(0,player.getEyeHeight(player.getPose()) - entity.getHeight() / 2 ,0).add(player.getRotationVector()));
        player.getServerWorld().spawnEntity(entity);
        Vec3d pos = entity.getPos();
        //player.getServerWorld().playSound(null, pos.x, pos.y + 1,pos.z , SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.PLAYERS);
        player.getServerWorld().playSound(null, pos.x, pos.y + 1,pos.z , SoundEvents.BLOCK_BEEHIVE_ENTER, SoundCategory.PLAYERS,0.5f,1.1f);

        //Vec3d rotation = player.getRotationVector();
        //ProjectileEntity.spawn(
        //        new MagicOrbEntity(ModEntities.MAGICORB,player.getServerWorld()), player.getServerWorld(), ItemStack.EMPTY,entity -> {
        //            entity.setVelocity(rotation.x, rotation.y, rotation.z, 1.6F, 12.0F);
        //            entity.setPos();
        //        }
        //);
        return true;
    }
}
