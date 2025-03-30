package com.avaris.avarisgates.core.api.config.option;

import com.avaris.avarisgates.core.api.config.ConfigContainer;
import com.google.gson.JsonPrimitive;
import org.jetbrains.annotations.Nullable;

/**
 * An abstract class used to create options of type {@code <T>}
 * @param <T> the type of the value associated with the option
 * @see ConfigContainer
 * @see BooleanOption
 * @see EnumOption
 * @see IntegerOption
 * @see StringOption
 */
public abstract class ConfigOption<T> {
    private final String name;
    private final T defaultValue;

    /**
     * Creates a new {@code ConfigOption} instance with the specified name and default value.
     * Sets the default value in {@link ConfigContainer}.
     * @param name the name of the configuration option must match the variable name (case-insensitive), to be saved properly
     * @param defaultValue the default value of the configuration option
     */
    public ConfigOption(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.setValue(this.defaultValue);
    }

    /**
     * Retrieves the current value associated with this option.
     *
     * @return the current value of type T
     */
    public abstract T getValue();

    /**
     * Retrieves the default value associated with this option.
     *
     * @return the default value of type T
     */
    public T getDefaultValue(){
        return this.defaultValue;
    }

    /**
     * Sets the specified value for the option.
     *
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public abstract boolean setValue(T value);

    /**
     * Retrieves the name of the option.
     * @return name of the option
     */
    public String getName(){
        return this.name;
    }

    /**
     * A utility function simplifying the JSON config loading process.
     * @return the value if it can be considered a JSON primitive, otherwise null
     */
    @Nullable
    public JsonPrimitive toJsonPrimitive(){
        return null;
    }
}
