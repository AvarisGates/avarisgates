package com.avaris.avarisgates.core.config.option;

import com.avaris.avarisgates.core.config.ConfigContainer;
import com.google.gson.JsonPrimitive;

public class StringOption extends ConfigOption<String> {

    /**
     * Creates a new {@code StringOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public StringOption(String name, String defaultValue) {
        super(name, defaultValue);
    }
    @Override
    public String getValue() {
        return ConfigContainer.getOptionS(this.getName());
    }

    @Override
    public boolean setValue(String value) {
        return ConfigContainer.setOptionS(this.getName(),value);
    }

    @Override
    public JsonPrimitive toJsonPrimitive() {
        return new JsonPrimitive(this.getValue());
    }
}
