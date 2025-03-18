package com.avaris.avarisgates.core.api.config.option;

import com.avaris.avarisgates.core.api.config.ConfigContainer;

public class EnumOption<E extends Enum<E>> extends ConfigOption<E> {
    private final Class<E> enumClass;

    /**
     * Creates a new {@code EnumOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public EnumOption(String name, E defaultValue) {
        super(name, defaultValue);
        this.enumClass = defaultValue.getDeclaringClass();
    }

    @Override
    public E getValue() {
        return ConfigContainer.getOptionE(this.getName(),this.enumClass);
    }

    @Override
    public boolean setValue(E value) {
        return ConfigContainer.setOptionE(this.getName(),value);
    }
}
