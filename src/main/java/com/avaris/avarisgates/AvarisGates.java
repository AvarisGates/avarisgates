package com.avaris.avarisgates;

import com.avaris.avarisgates.core.AttributeFix;
import com.avaris.avarisgates.core.ModComponents;
import com.avaris.avarisgates.core.command.ModCommands;
import com.avaris.avarisgates.core.config.AbstractConfigManager;
import com.avaris.avarisgates.core.config.JsonConfigManager;
import com.avaris.avarisgates.core.config.ModConfig;
import com.avaris.avarisgates.core.config.PropertiesConfigManager;
import com.avaris.avarisgates.core.currency.CurrencyAttachment;
import com.avaris.avarisgates.core.dungeon.DungeonManager;
import com.avaris.avarisgates.core.entity.ModEntities;
import com.avaris.avarisgates.core.event.ConfigEvents;
import com.avaris.avarisgates.core.event.ModLifecycleEvents;
import com.avaris.avarisgates.core.item.ModItems;
import com.avaris.avarisgates.core.network.ModPackets;
import com.avaris.avarisgates.core.player.ManaAttachment;
import com.avaris.avarisgates.core.player.ability.AbilityRegistrar;
import com.avaris.avarisgates.core.player.ability.AbilitySlot;
import com.avaris.avarisgates.core.player.attribute.Attribute;
import com.avaris.avarisgates.core.player.attribute.AttributeType;
import com.avaris.avarisgates.core.player.player_class.PlayerClass;
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

    // Set default config manager here, use any implementation
    public static final AbstractConfigManager CONFIG_MANAGER = PropertiesConfigManager.getInstance();

    @Override
    public void onInitialize() {

        ModLifecycleEvents.INITIALIZE_EVENT.invoker().onInitialize();

        // Init fabric attachments
        AbilitySlot.init();
        PlayerClass.init();
        AttributeType.init();
        ManaAttachment.init();
        CurrencyAttachment.init();

        CONFIG_MANAGER.loadConfig();

        ModComponents.init();
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
            // This is kind of inefficient
            minecraftServer.getPlayerManager().getPlayerList().forEach(Attribute::updateAttributes);
        });
        ServerLifecycleEvents.SERVER_STOPPING.register((minecraftServer -> {
            dungeonManager.removeAllDungeons();
        }));

        ModLifecycleEvents.INITIALIZED_EVENT.invoker().onInitialized();
    }


}

