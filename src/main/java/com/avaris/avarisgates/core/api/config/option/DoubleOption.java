package com.avaris.avarisgates.core.api.config.option;

import com.avaris.avarisgates.core.api.config.ConfigContainer;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

public class DoubleOption extends ConfigOption<Double> {

    /**
     * Creates a new {@code DoubleOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public DoubleOption(String name, double defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Double getValue() {
        return ConfigContainer.getOptionD(this.getName());
    }

    @Override
    public boolean setValue(Double value) {
        return ConfigContainer.setOptionD(this.getName(), value);
    }

    @Override
    public @Nullable JsonPrimitive toJsonPrimitive() {
        return new JsonPrimitive(this.getValue());
    }
}
