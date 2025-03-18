package com.avaris.avarisgates.core.api.config;

import com.avaris.avarisgates.ModConfig;
import com.avaris.avarisgates.core.api.config.option.*;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

/**
 * An implementation of{@link AbstractConfigManager}used for saving and loading JSON config files.
 * @see ConfigContainer
 * @see IModConfig
 */
public class JsonConfigManager extends AbstractConfigManager {
    private static final Gson GSON = new Gson().newBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_NAME = "avarisgates.json";
    private static final Logger LOGGER = LoggerFactory.getLogger("AvarisGates|JsonConfigManager");
    private static final JsonConfigManager INSTANCE = new JsonConfigManager(ModConfig.class);

    protected JsonConfigManager(Class<? extends IModConfig> modConfigClass) {
        super(modConfigClass);
    }

    public static JsonConfigManager getInstance(){
        return INSTANCE;
    }

    @Override
    public Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected String getConfigFileName() {
        return CONFIG_FILE_NAME;
    }

    @Override
    public boolean loadConfig() {
        if(!setupConfigPath()){
            return false;
        }

        if(!Files.exists(configPath)){
            this.getLogger().warn("Config file not found, creating a new one, with default values.");
            saveConfig();
            return true;
        }
        try{
            BufferedReader reader = Files.newBufferedReader(configPath);
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            if(json == null){
                this.getLogger().warn("Config file is empty, creating a new one, with default values.");
                saveConfig();
                return true;
            }
            boolean errorFound = false;
            boolean configChanged = false;
            // If an error occurs we want to roll back the changes
            if(!ConfigContainer.beginTransaction()){
                this.getLogger().warn("ConfigContainer is already in a transaction, this may cause issues.");
            }

            for(Field field : this.modConfigClass.getDeclaredFields()){
                if(!this.shouldSaveField(field)){
                    continue;
                }

                String fieldName = field.getName().toLowerCase(Locale.ROOT);

                if(BooleanOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected Boolean",fieldName);
                        errorFound = true;
                        continue;
                    }

                    boolean value = element.getAsBoolean();
                    if(!Objects.equals(value,ConfigContainer.getOptionB(fieldName))){
                        configChanged = true;
                    }
                    ConfigContainer.setOptionB(fieldName,value);
                }else if(StringOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected String",fieldName);
                        errorFound = true;
                        continue;
                    }

                    String value = element.getAsString();
                    if(!Objects.equals(value,ConfigContainer.getOptionS(fieldName))){
                        configChanged = true;
                    }
                    ConfigContainer.setOptionS(fieldName,value);
                } else if(IntegerOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("{} not found in config file.",fieldName);
                        errorFound = true;
                        continue;
                    }
                    try{
                        int value = element.getAsInt();
                        if(!Objects.equals(value,ConfigContainer.getOptionI(fieldName))){
                            configChanged = true;
                        }
                        ConfigContainer.setOptionI(fieldName,value);
                    }catch (UnsupportedOperationException | NumberFormatException | IllegalStateException e){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected Integer",fieldName);
                        errorFound = true;
                    }
                }else if(EnumOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("{} not found in config file.",fieldName);
                    }
                    JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive(field.getName()
                            .toLowerCase(Locale.ROOT));
                    if (jsonPrimitive != null && jsonPrimitive.isString()) {
                        Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                        if (generic instanceof Class<?>) {
                            EnumOption<?> option = (EnumOption<?>) field.get(null);
                            Enum<?> found = null;
                            List<String> valid_values = new ArrayList<>();
                            for (Enum<?> value : ((Class<Enum<?>>) generic).getEnumConstants()) {
                                String value_name = value.name().toLowerCase(Locale.ROOT);
                                valid_values.add("\"" + value_name + "\"");
                                if (value_name.equals(jsonPrimitive.getAsString())) {
                                    found = value;
                                    break;
                                }
                            }
                            if (found != null) {
                                ConfigContainer.setOptionERaw(option.getName(), found);
                            } else {
                                this.getLogger().warn("Config file contains an invalid value for {}. Expected one of: {}",fieldName,valid_values);
                                errorFound = true;
                            }
                        }
                    }
                }else if(FloatOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("{} not found in config file.",fieldName);
                        errorFound = true;
                        continue;
                    }
                    try{
                        float value = element.getAsFloat();
                        if(!Objects.equals(value,ConfigContainer.getOptionF(fieldName))){
                            configChanged = true;
                        }
                        ConfigContainer.setOptionF(fieldName,value);
                    }catch (UnsupportedOperationException | NumberFormatException | IllegalStateException e){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected Float",fieldName);
                        errorFound = true;
                    }
                }else if(DoubleOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("{} not found in config file.",fieldName);
                        errorFound = true;
                        continue;
                    }
                    try{
                        double value = element.getAsDouble();
                        if(!Objects.equals(value,ConfigContainer.getOptionD(fieldName))){
                            configChanged = true;
                        }
                        ConfigContainer.setOptionD(fieldName,value);
                    }catch (UnsupportedOperationException | NumberFormatException | IllegalStateException e){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected Double",fieldName);
                        errorFound = true;
                    }
                }else if(LongOption.class.isAssignableFrom(field.getType())){
                    JsonElement element = json.get(fieldName);
                    if(element == null || !element.isJsonPrimitive()){
                        this.getLogger().warn("{} not found in config file.",fieldName);
                        errorFound = true;
                        continue;
                    }
                    try{
                        Long value = element.getAsLong();
                        if(!Objects.equals(value,ConfigContainer.getOptionL(fieldName))){
                            configChanged = true;
                        }
                        ConfigContainer.setOptionL(fieldName,value);
                    }catch (UnsupportedOperationException | NumberFormatException | IllegalStateException e){
                        this.getLogger().warn("Config file contains an invalid value for {}. Expected Long",fieldName);
                        errorFound = true;
                    }
                }
            }

            if(!ConfigContainer.endTransaction(errorFound)){
                this.getLogger().warn("ConfigContainer not in a transaction on ConfigContainer.endTransaction call.");
            }
            if(errorFound){
                this.getLogger().warn("Error in config file, rolling back changes");
                saveConfig();
                return false;
            }

            String additionalMessage = ".";
            if(!configChanged){
                additionalMessage = ", with no changes.";
            }
            this.getLogger().info("Loaded config{}",additionalMessage);

        }catch (IOException e){
            this.getLogger().error("Failed to read config file:\n{}",e.getMessage());
            return false;
        }catch (IllegalStateException e){
            this.getLogger().error("Error in config file, rolling back changes:\n{}",e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return super.loadConfig();
    }

    @Override
    public boolean saveConfig() {
        if(!setupConfigPath()){
            return false;
        }
        JsonObject json = new JsonObject();
        try{
            for(Field field : this.modConfigClass.getDeclaredFields()){
                if(!this.shouldSaveField(field)){
                    continue;
                }
                String fieldName = field.getName().toLowerCase(Locale.ROOT);
                ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                JsonPrimitive primitive = option.toJsonPrimitive();
                if(primitive != null){
                    json.add(fieldName, primitive);
                }
                else if(EnumOption.class.isAssignableFrom(field.getType())){
                    Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if(generic instanceof Class<?>){
                        EnumOption<?> enumOption = (EnumOption<?>) field.get(null);
                        json.add(fieldName, new JsonPrimitive(enumOption.getValue().name().toLowerCase(Locale.ROOT)));
                    }else{
                        this.getLogger().warn("EnumOption generic type is not a Class<?> instance, skipping field {}.",fieldName);
                    }
                }
            }
        }catch(IllegalAccessException e){
            e.printStackTrace();
            return false;
        }

        try{
            BufferedWriter writer = Files.newBufferedWriter(configPath);
            writer.write(GSON.toJson(json));
            writer.close();
        } catch (IOException e) {
            this.getLogger().error("Failed to write config file:\n{}",e.getMessage());
            return false;
        }
        return super.saveConfig();
    }


}
