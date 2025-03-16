package com.avaris.avarisgates.core.config.option;

import com.avaris.avarisgates.core.config.ConfigContainer;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class FloatOption extends ConfigOption<Float> {

    /**
     * Creates a new {@code FloatOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public FloatOption(String name, float defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Float getValue() {
        return ConfigContainer.getOptionF(this.getName());
    }

    @Override
    public boolean setValue(Float value) {
        return ConfigContainer.setOptionF(this.getName(), value);
    }

    @Override
    public @Nullable JsonPrimitive toJsonPrimitive() {
        return new JsonPrimitive(this.getValue());
    }
}
