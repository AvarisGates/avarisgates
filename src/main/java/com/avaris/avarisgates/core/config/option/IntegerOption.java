package com.avaris.avarisgates.core.config.option;

import com.avaris.avarisgates.core.config.ConfigContainer;
import org.jetbrains.annotations.Nullable;

public class IntegerOption extends ConfigOption<Integer> {

    /**
     * Creates a new {@code IntegerOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public IntegerOption(String name, int defaultValue) {
        super(name, defaultValue);
    }

    @Override
    public Integer getValue() {
        return ConfigContainer.getOptionI(this.getName());
    }

    @Override
    public boolean setValue(Integer value) {
        return ConfigContainer.setOptionI(this.getName(), value);
    }

    @Override
    public @Nullable Class<?> getJsonPrimitiveType() {
        return Integer.class;
    }
}
