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
    public static final EnumOption<UGood> U_GOOD = new EnumOption<>("U_GOOD", UGood.Yeeeee);

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
