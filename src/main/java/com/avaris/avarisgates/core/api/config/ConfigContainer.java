package com.avaris.avarisgates.core.api.config;

import com.avaris.avarisgates.core.api.config.option.ConfigOption;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Stores all options, alongside their values, allowing
 * getting and setting options using their names as keys.
 * <br><br>
 * Method naming convention:
 *<br> - getOptionX - retrieve an option of type X
 *<br> - setOptionX - set the value of an option of type X
 *<br> if the type is commonly used abbreviate it to one capital letter ex. S - String, B - Boolean
 * @see ConfigOption
 */
public class ConfigContainer {
    private static final ConfigMap<Boolean> booleanOptions = new ConfigMap<>();
    private static final ConfigMap<String> stringOptions = new ConfigMap<>();
    private static final ConfigMap<Integer> integerOptions = new ConfigMap<>();
    private static final ConfigMap<Enum<?>> enumOptions = new ConfigMap<>();
    private static final ConfigMap<Float> floatOptions = new ConfigMap<>();
    private static final ConfigMap<Double> doubleOptions = new ConfigMap<>();
    private static final ConfigMap<Long> longOptions = new ConfigMap<>();

    private static boolean isInTransaction = false;

    /**
     * Starts a transaction.
     * When in a transaction config changes are saved in temporary variables.
     * When{@link ConfigContainer#endTransaction(boolean)}is called the transaction ends and changes are saved or discarded.
     * @return true if a transaction started, false if a transaction has been started previously.
     */
    public static boolean beginTransaction(){
        if(isInTransaction){
            return false;
        }
        isInTransaction = true;
        booleanOptions.beginTransaction();
        stringOptions.beginTransaction();
        integerOptions.beginTransaction();
        enumOptions.beginTransaction();
        floatOptions.beginTransaction();
        doubleOptions.beginTransaction();
        longOptions.beginTransaction();
        return true;
    }

    /**
     * If in a transaction end the current transaction otherwise does nothing, and returns false.
     * @param discard if set to true changes are not saved.
     * @return true if a transaction ended, false if not in a transaction.
     * @see ConfigContainer#beginTransaction()
     */
    public static boolean endTransaction(boolean discard){
        if(!isInTransaction){
            return false;
        }
        isInTransaction = false;

        booleanOptions.endTransaction(discard);
        stringOptions.endTransaction(discard);
        integerOptions.endTransaction(discard);
        enumOptions.endTransaction(discard);
        floatOptions.endTransaction(discard);
        doubleOptions.endTransaction(discard);
        longOptions.endTransaction(discard);
        return true;
    }

    /**
     * Retrieves the value of a Boolean option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    @Nullable
    public static Boolean getOptionB(String name) {
        return booleanOptions.get(name);
    }

    /**
     * Sets the specified value for the Boolean option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionB(@NotNull String name, boolean value) {
        return booleanOptions.put(name,value) != null;
    }

    /**
     * Retrieves the value of a String option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    @Nullable
    public static String getOptionS(String name) {
        return stringOptions.get(name);
    }

    /**
     * Sets the specified value for the String option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionS(@NotNull String name, String value) {
        return stringOptions.put(name, value) != null;
    }

    /**
     * Retrieves the value of an Integer option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    @Nullable
    public static Integer getOptionI(String name) {
        return integerOptions.get(name);
    }

    /**
     * Sets the specified value for the Integer option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionI(String name, Integer value) {
        return integerOptions.put(name, value) != null;
    }

    /**
     * Retrieves the value of an Enum option.
     * @param name name of the option
     * @param enumClass the class of the enum
     * @return value of the option or null if the option doesn't exist
     */
    public static <E extends Enum<E>> E getOptionE(String name, Class<E> enumClass) {
        return (E) enumOptions.get(name);
    }

    /**
     * Retrieves the value of an Enum option, can be called without knowing the enum class - hence 'raw'.
     * @param name name of the option
     * @param enumClass the class of the enum
     * @return value of the option or null if the option doesn't exist
     */
    public static Enum<?> getOptionERaw(String name, Class<Enum<?>> enumClass) {
        return enumOptions.get(name);
    }

    /**
     * Sets the specified value for the Enum option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     * @see ConfigContainer#setOptionERaw(String, Enum) 
     */
    public static <E extends Enum<E>> boolean setOptionE(String name, E value) {
        return enumOptions.put(name, value) != null;
    }

    /**
     * Sets the specified value for the Enum option, can be called without knowing the enum class - hence 'raw'.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionERaw(String name, Enum<?> value) {
        return enumOptions.put(name, value) != null;
    }

    /**
     * Retrieves the value of a Float option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    public static Float getOptionF(String name) {
        return floatOptions.get(name);
    }

    /**
     * Sets the specified value for the Float option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionF(String name, Float value) {
        return floatOptions.put(name, value) != null;
    }

    /**
     * Retrieves the value of a Double option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    public static Double getOptionD(String name) {
        return doubleOptions.get(name);
    }

    /**
     * Sets the specified value for the Double option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionD(String name, Double value) {
        return doubleOptions.put(name, value) != null;
    }

    /**
     * Retrieves the value of a Long option.
     * @param name name of the option
     * @return value of the option or null if the option doesn't exist
     */
    public static Long getOptionL(String name) {
        return longOptions.get(name);
    }

    /**
     * Sets the specified value for the Long option.
     * If in a transaction the option is saved in a temporary variable.
     * @param name name of the option
     * @param value the new value to be set for the option
     * @return true if the option had a previous value, otherwise false
     */
    public static boolean setOptionL(String name, Long value) {
        return longOptions.put(name, value) != null;
    }
}
