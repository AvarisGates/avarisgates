package com.avaris.avarisgates.core.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ConfigEvents {
    public static final Event<ConfigLoaded> CONFIG_LOADED_EVENT = EventFactory.createArrayBacked(ConfigLoaded.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onConfigLoaded();
        }
    });

    public static final Event<ConfigSaved> CONFIG_SAVED_EVENT = EventFactory.createArrayBacked(ConfigSaved.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onConfigSaved();
        }
    });

    @FunctionalInterface
    public interface ConfigLoaded {
        void onConfigLoaded();
    }

    @FunctionalInterface
    public interface ConfigSaved{
        void onConfigSaved();
    }
}
