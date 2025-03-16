package com.avaris.avarisgates.core.config.option;

import com.avaris.avarisgates.core.config.ConfigContainer;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class BooleanOption extends ConfigOption<Boolean> {

    /**
     * Creates a new {@code BooleanOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public BooleanOption(String name, boolean defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Boolean getValue() {
        return ConfigContainer.getOptionB(this.getName());
    }

    @Override
    public boolean setValue(Boolean value) {
        return ConfigContainer.setOptionB(this.getName(), value);
    }

    @Override
    public @Nullable JsonPrimitive toJsonPrimitive() {
        return new JsonPrimitive(this.getValue());
    }
}
