package com.avaris.avarisgates;

import com.avaris.scribecodex.Scribe;
import com.avaris.scribecodex.api.v1.impl.option.*;
import com.avaris.scribecodex.api.v1.IModConfig;

public class ModConfig implements IModConfig {

    public static final Scribe SCRIBE = new Scribe(ModConfig.class,AvarisGates.MOD_ID);

    public static final BooleanOption DEBUG_MODE = SCRIBE.createOption("debug_mode", true);
    public static final StringOption HOW_WE_FEELIN = SCRIBE.createOption ("how_we_feelin", "good");
    public static final IntegerOption AM_INTEGER = SCRIBE.createOption("am_integer", -7331);

    public static final EnumOption<UGood> U_GOOD = SCRIBE.createOption("u_good", UGood.Yeeeee);

    public static final FloatOption AM_FLOAT = SCRIBE.createOption("am_float", 0.0000123f);
    public static final DoubleOption AM_DOUBLE = SCRIBE.createOption("am_double",1.24442424110001111);
    public static final LongOption AM_LONG = SCRIBE.createOption("am_long", 1234567890123456789L);
    public static final BooleanOption PRINT_ATTRIBUTE_FIX_INFO = SCRIBE.createOption("print_attribute_fix_info",false);

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
