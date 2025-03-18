package com.avaris.avarisgates.core.api.event;

import com.avaris.avarisgates.AvarisGates;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ConfigEvents {
    public static final Event<ConfigLoaded> CONFIG_LOADED_EVENT = EventFactory.createArrayBacked(ConfigLoaded.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onConfigLoaded();
        }
    });

    static {
        ConfigEvents.CONFIG_LOADED_EVENT.register(() -> {
            AvarisGates.CONFIG_MANAGER.getLogger().info("Config successfully loaded");
            AvarisGates.CONFIG_MANAGER.printConfig();
        });
    }

    public static final Event<ConfigSaved> CONFIG_SAVED_EVENT = EventFactory.createArrayBacked(ConfigSaved.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onConfigSaved();
        }
    });

    static {
        ConfigEvents.CONFIG_SAVED_EVENT.register(() -> {
            AvarisGates.CONFIG_MANAGER.getLogger().info("Config successfully saved");
            AvarisGates.CONFIG_MANAGER.printConfig();
        });
    }

    @FunctionalInterface
    public interface ConfigLoaded {
        void onConfigLoaded();
    }

    @FunctionalInterface
    public interface ConfigSaved{
        void onConfigSaved();
    }
}
