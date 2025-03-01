package com.avaris.avarisgates;

import com.avaris.avarisgates.core.AttributeFix;
import com.avaris.avarisgates.core.command.ModCommands;
import com.avaris.avarisgates.core.dungeon.DungeonManager;
import com.avaris.avarisgates.core.entity.ModEntities;
import com.avaris.avarisgates.core.item.ModItems;
import com.avaris.avarisgates.core.network.ModPackets;
import com.avaris.avarisgates.core.player.ManaAttachment;
import com.avaris.avarisgates.core.player.ability.AbilityRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AvarisGates implements ModInitializer {

    public static String MOD_ID = "avarisgates";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Identifier id(String id){
        return Identifier.of(MOD_ID,id);
    }

    public static DungeonManager dungeonManager = new DungeonManager();

    @Override
    public void onInitialize() {
        ModItems.init();
        AbilityRegistrar.init();
        AttributeFix.init();
        ModPackets.init();
        ModEntities.init();
        ModCommands.init();
        ServerTickEvents.END_SERVER_TICK.register((minecraftServer)->{
            dungeonManager.tick(minecraftServer);
            if(minecraftServer.getTicks() % 20 == 0){
                minecraftServer.getPlayerManager().getPlayerList().forEach(ManaAttachment::tickMana);
            }
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer -> {
            dungeonManager.removeAllDungeons();
        }));
    }


}

