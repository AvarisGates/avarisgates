package com.avaris.avarisgates.core.api.config;

import com.avaris.avarisgates.ModConfig;
import com.avaris.avarisgates.core.api.config.option.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * An implementation of{@link AbstractConfigManager}used for saving and loading the java properties config files.
 * @see ConfigContainer
 * @see IModConfig
 */
public class PropertiesConfigManager extends AbstractConfigManager{
    private static final String CONFIG_FILE_NAME = "avarisgates.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger("AvarisGates|PropertiesConfigManager");
    private static final PropertiesConfigManager INSTANCE = new PropertiesConfigManager(ModConfig.class);

    protected PropertiesConfigManager(Class<? extends IModConfig> modConfigClass) {
        super(modConfigClass);
    }

    public static PropertiesConfigManager getInstance(){
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
            BufferedReader reader = Files.newBufferedReader(this.configPath);
            Properties properties = new Properties();
            properties.load(reader);
            reader.close();
            ConfigContainer.beginTransaction();
            for(Field field : this.modConfigClass.getDeclaredFields()){
                if(!this.shouldSaveField(field)){
                    continue;
                }
                String fieldName = field.getName().toLowerCase(Locale.ROOT);
                String property = properties.getProperty(fieldName);
                if(property == null){
                    this.getLogger().warn("{} not set, using default value",fieldName);
                    continue;
                }
                if(BooleanOption.class.isAssignableFrom(field.getType())){
                    BooleanOption option = (BooleanOption) field.get(null);
                    option.setValue(Boolean.parseBoolean(property));
                }else if(DoubleOption.class.isAssignableFrom(field.getType())){
                    DoubleOption option = (DoubleOption) field.get(null);
                    option.setValue(Double.parseDouble(property));
                }else if(EnumOption.class.isAssignableFrom(field.getType())){
                    EnumOption<?> option = (EnumOption<?>)field.get(null);
                    //option.setValue(option.getValue()) :
                    Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                    if (generic instanceof Class<?>) {
                        Enum<?> found = null;
                        List<String> valid_values = new ArrayList<>();
                        for (Enum<?> value : ((Class<Enum<?>>) generic).getEnumConstants()) {
                            String value_name = value.name().toLowerCase(Locale.ROOT);
                            valid_values.add("\"" + value_name + "\"");
                            if (value_name.equals(property)) {
                                found = value;
                                break;
                            }
                        }
                        if (found != null) {
                            ConfigContainer.setOptionERaw(option.getName(), found);
                        } else {
                            this.getLogger().warn("Config file contains an invalid value for {}. Expected one of: {}", fieldName, valid_values);
                        }
                    }
                }else if(FloatOption.class.isAssignableFrom(field.getType())){
                    FloatOption option = (FloatOption) field.get(null);
                    option.setValue(Float.parseFloat(property));
                }else if(IntegerOption.class.isAssignableFrom(field.getType())){
                    IntegerOption option = (IntegerOption) field.get(null);
                    option.setValue(Integer.parseInt(property));
                }else if(LongOption.class.isAssignableFrom(field.getType())){
                    LongOption option = (LongOption) field.get(null);
                    option.setValue(Long.parseLong(property));
                }else if(StringOption.class.isAssignableFrom(field.getType())){
                    StringOption option = (StringOption) field.get(null);
                    option.setValue(property);
                }
            }
            ConfigContainer.endTransaction(false);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return super.loadConfig();
    }

    @Override
    public boolean saveConfig() {
        Properties properties = new Properties();
        try{
            for(Field field : modConfigClass.getDeclaredFields()){
                String fieldName = field.getName().toLowerCase(Locale.ROOT);
                ConfigOption<?> option = (ConfigOption<?>) field.get(null);
                properties.put(fieldName,option.getValue().toString().toLowerCase(Locale.ROOT));
            }
            BufferedWriter writer = Files.newBufferedWriter(this.configPath);
            properties.store(writer,"");
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return super.saveConfig();
    }
}
