package com.avaris.avarisgates.core.config;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

/**
 * A wrapper around a{@link HashMap}with{@link String}as the key and{@link T} as the value.<br>
 * Allows for rolling back changes using transactions.
 * @see ConfigMap#beginTransaction()
 * @see ConfigMap#endTransaction(boolean)
 */
public class ConfigMap<T> {
    private final HashMap<String,T> map = new HashMap<>();
    private final HashMap<String,T> tmpMap = new HashMap<>();
    private boolean isInTransaction = false;

    /**
     * Sets the specified value for the Boolean option.
     * If in a transaction the option is saved in a temporary variable.
     * @param key name of the option
     * @param value the new value to be set for the option
     * @return the previous value
     */
    @Nullable
    public T put(String key, T value){
        if(isInTransaction){
            return tmpMap.put(key,value);
        }
        return map.put(key,value);
    }
    /**
     * Retrieves the value mapped to the key.
     * @param key name of the option
     * @return value of the option or null if the option doesn't exist
     */
    @Nullable
    public T get(String key){
        return map.get(key);
    }

    /**
     * Starts a transaction.
     * When in a transaction config changes are saved in temporary variables.
     * When{@link ConfigMap#endTransaction(boolean)}is called the transaction ends and changes are saved or discarded.
     */
    public void beginTransaction(){
        isInTransaction = true;
        tmpMap.clear();
    }

    /**
     * Ends the current transaction.
     * @param discard if set to true changes are not saved.
     * @see ConfigMap#beginTransaction()
     */
    public void endTransaction(boolean discard){
        isInTransaction = false;
        if(!discard){
            map.putAll(tmpMap);
        }
        tmpMap.clear();
    }
}
