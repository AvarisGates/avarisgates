package com.avaris.avarisgates;

import com.avaris.scribecodex.Scribe;
import com.avaris.scribecodex.api.v1.impl.option.*;
import com.avaris.scribecodex.api.v1.IModConfig;
import com.avaris.scribecodex.api.v1.option.*;

public class ModConfig implements IModConfig {

    public static final Scribe SCRIBE = new Scribe(ModConfig.class,AvarisGates.MOD_ID);

    @IBooleanOption(defaultValue = true)
    public static BooleanOption DEBUG_MODE;

    @IStringOption(defaultValue = "good")
    public static StringOption HOW_WE_FEELIN;

    @IIntegerOption(defaultValue = -7331)
    public static IntegerOption AM_INTEGER;

    public static EnumOption<UGood> U_GOOD = SCRIBE.createOption("u_good", UGood.Yeeeee);

    @IFloatOption(defaultValue = 0.0000123f)
    public static FloatOption AM_FLOAT;

    @IDoubleOption(defaultValue = 1.24442424110001111)
    public static DoubleOption AM_DOUBLE;

    @ILongOption(defaultValue = 1234567890123456789L)
    public static LongOption AM_LONG;

    @IBooleanOption(defaultValue = false)
    public static BooleanOption PRINT_ATTRIBUTE_FIX_INFO;

    public enum UGood {
        Yeeeee,
        Naaahhh,
        IDK,
        Maybe,
        IHaveNoIdea;
    }

    public static void init(){
        SCRIBE.loadConfig();
        if(ModConfig.DEBUG_MODE.getValue()){
            SCRIBE.printDebugInfo();
        }
    }
}
