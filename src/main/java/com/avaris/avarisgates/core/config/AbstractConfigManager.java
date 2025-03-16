package com.avaris.avarisgates.core.config;

import com.avaris.avarisgates.core.config.option.ConfigOption;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.nio.file.Path;

/**
 * An abstract class defining the basic functionality of a config manager - saving and loading config files.<br>
 * It's made with the intention to have the option to save config files with different file types (extensions), using different implementations.<br>
 * Only implements{@link AbstractConfigManager#shouldSaveField(Field)},{@link AbstractConfigManager#printConfig()}and{@link AbstractConfigManager#setupConfigPath()}.
 * Other methods must be implemented by the subclass.
 * @see JsonConfigManager
 * @see ConfigContainer
 */
public abstract class AbstractConfigManager {
    /**
     * Path to the config file, setup by the {@link AbstractConfigManager#setupConfigPath()} function.
     * Each ConfigManager subclass can have different paths (mostly for different extensions).
     */
    protected Path configPath = null;

    /**
     * This method allows us to create a specific logger for each ConfigManager,
     * with a different name.
     * @return Logger instance associated with the ConfigManager subclass
     */
    abstract protected Logger getLogger();

    /**
     * This method allows us to use different files for each ConfigManager,
     * @return name of the config file associated with the ConfigManager subclass
     */
    abstract protected String getConfigFileName();

    /**
     * Loads config from the {@link AbstractConfigManager#configPath}.
     * @return true if the config was successfully loaded, otherwise false
     */
    abstract boolean loadConfig();

    /**
     * Saves config to the {@link AbstractConfigManager#configPath}.
     * @return true if the config was successfully saved, otherwise false
     */
    abstract boolean saveConfig();

    /**
     * Check if a field should be saved/loaded to/from the config file
     * @param field the Field to validate.
     * @return whether the field should be saved/loaded
     */
    boolean shouldSaveField(Field field){
        return true;
    }

    /**
     * Sets up the configuration file path, if the path isn't already set.
     * Should be called before accessing the {@link AbstractConfigManager#configPath}.
     * @return true if the config file path was successfully set up or the file already exists,
     *         false if there was an error while creating the configuration file.
     * @see AbstractConfigManager#configPath
     */
    protected boolean setupConfigPath(){
        ModConfig.init();
        if(configPath != null){
            this.getLogger().info("Config path found: {}",this.configPath);
            return true;
        }
        this.configPath = FabricLoader.getInstance().getConfigDir().resolve(this.getConfigFileName());
        this.getLogger().info("Config allocated path: {}",this.configPath);
        return true;
    }

    /**
     * A debug method.
     * Prints the config options from the{@link ModConfig}class.
     */
    public void printConfig(){
        for(Field field : ModConfig.class.getDeclaredFields()){
            if(!this.shouldSaveField(field)){
                continue;
            }
            try {
                ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                this.getLogger().info("{}: {}",field.getName(),option.getValue());
            }catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
    }

}
