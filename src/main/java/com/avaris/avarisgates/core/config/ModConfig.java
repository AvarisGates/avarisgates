package com.avaris.avarisgates.core.config;

import com.avaris.avarisgates.core.config.option.*;

/**
 * This class houses options used by the mod.<br>
 * To get an option's value call {@code OPTION_NAME.getValue()} <br>
 * For example:<br>
 * {@code DEBUG_MODE.getValue();}<br>
 * To set an option's value call {@code OPTION_NAME.setValue(newValue)} <br>
 * For example:<br>
 * {@code DEBUG_MODE.setValue(false);}<br><br>
 *
 * To add a new option add a filed to this class of your type choice,<br>
 * initialized with the type's constructor, option name and default value.<br>
 * Option name MUST match the field name (case-insensitive).<br>
 * For example:<br>
 * {@code public static final BooleanOption DEBUG_MODE = new BooleanOption("debug_mode",true);}<br>
 * Notice that the field name -{@code DEBUG_MODE}matches the option name -{@code "debug_mode"}.<br>
 * This is all you need. Now the option will be saved, loaded and accessible at runtime.<br><br>
 *
 *
 * Options are loaded using{@link AbstractConfigManager#loadConfig()}
 * (use an implementation ex:{@link JsonConfigManager})<br><br>
 *
 * To save options use{@link AbstractConfigManager#saveConfig()}
 * (use an implementation)<br><br>
 *
 * Implementation details:<br>
 *
 * {@link ConfigOption}s use{@link ConfigContainer}to store their values during runtime.<br>
 *
 * The {@link ModConfig#init()} method is intended to ensure that the static fields
 * are initialized.
 * @see AbstractConfigManager
 * @see ConfigContainer
 */
public class ModConfig {
    public static final BooleanOption DEBUG_MODE = new BooleanOption("debug_mode", true);
    public static final StringOption HOW_WE_FEELIN = new StringOption("how_we_feelin", "good");
    public static final IntegerOption AM_INTEGER = new IntegerOption("am_integer", -7331);
    public static final EnumOption<UGood> U_GOOD = new EnumOption<>("u_good", UGood.Yeeeee);
    public static final FloatOption AM_FLOAT = new FloatOption("am_float", 0.0000123f);
    public static final DoubleOption AM_DOUBLE = new DoubleOption("am_double",1.24442424110001111);
    public static final LongOption AM_LONG = new LongOption("am_long", 1234567890123456789L);
    public static final BooleanOption PRINT_ATTRIBUTE_FIX_INFO = new BooleanOption("print_attribute_fix_info",false);

    public enum UGood {
        Yeeeee,
        Naaahhh,
        IDK,
        Maybe,
        IHaveNoIdea;
    }

    // Another dummy function to initialize static members
    public static void init() {
    }
}
