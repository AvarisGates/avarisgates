package com.avaris.avarisgates.core.event;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

public class ModLifecycleEvents {
    public static final Event<Initialize> INITIALIZE_EVENT = EventFactory.createArrayBacked(Initialize.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onInitialize();
        }
    });

    public static final Event<Initialized> INITIALIZED_EVENT = EventFactory.createArrayBacked(Initialized.class,(callbacks) -> () -> {
        for(var callback : callbacks){
            callback.onInitialized();
        }
    });

    @FunctionalInterface
    public interface Initialize {
        void onInitialize();
    }

    @FunctionalInterface
    public interface Initialized{
        void onInitialized();
    }
}
