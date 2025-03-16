package com.avaris.avarisgates.core.config.option;

import com.avaris.avarisgates.core.config.ConfigContainer;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class LongOption extends ConfigOption<Long> {

    /**
     * Creates a new {@code LongOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public LongOption(String name, long defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Long getValue() {
        return ConfigContainer.getOptionL(this.getName());
    }

    @Override
    public boolean setValue(Long value) {
        return ConfigContainer.setOptionL(this.getName(), value);
    }

    @Override
    public @Nullable JsonPrimitive toJsonPrimitive() {
        return new JsonPrimitive(this.getValue());
    }
}
