package com.optiontype;

import com.optiontype.model.AsianAveragingType;
import com.optiontype.model.BarrierType;
import com.optiontype.model.BinaryType;
import com.optiontype.model.LookbackType;
import com.optiontype.model.OptionBasicType;
import com.optiontype.model.RainbowType;
import com.optiontype.model.Side;
import com.optiontype.model.OptionStyle;

public final class Prelude {
    private Prelude() {}

    public static final Class<OptionType>          OPTION_TYPE            = OptionType.class;
    public static final Class<OptionType.European> EUROPEAN               = OptionType.European.class;
    public static final Class<OptionType.American> AMERICAN               = OptionType.American.class;
    public static final Class<OptionType.Bermuda>  BERMUDA                = OptionType.Bermuda.class;
    public static final Class<OptionType.Asian>    ASIAN                  = OptionType.Asian.class;
    public static final Class<OptionType.Barrier>  BARRIER                = OptionType.Barrier.class;
    public static final Class<OptionType.Binary>   BINARY                 = OptionType.Binary.class;
    public static final Class<OptionType.Lookback> LOOKBACK               = OptionType.Lookback.class;
    public static final Class<OptionType.Compound> COMPOUND               = OptionType.Compound.class;
    public static final Class<OptionType.Chooser>  CHOOSER                = OptionType.Chooser.class;
    public static final Class<OptionType.Cliquet>  CLIQUET                = OptionType.Cliquet.class;
    public static final Class<OptionType.Rainbow>  RAINBOW                = OptionType.Rainbow.class;
    public static final Class<OptionType.Spread>   SPREAD                 = OptionType.Spread.class;
    public static final Class<OptionType.Quanto>   QUANTO                 = OptionType.Quanto.class;
    public static final Class<OptionType.Exchange> EXCHANGE               = OptionType.Exchange.class;
    public static final Class<OptionType.Power>    POWER                  = OptionType.Power.class;
    public static final Class<AsianAveragingType>  ASIAN_AVERAGING_TYPE   = AsianAveragingType.class;
    public static final Class<BarrierType>         BARRIER_TYPE           = BarrierType.class;
    public static final Class<BinaryType>          BINARY_TYPE            = BinaryType.class;
    public static final Class<LookbackType>        LOOKBACK_TYPE          = LookbackType.class;
    public static final Class<RainbowType>         RAINBOW_TYPE           = RainbowType.class;
    public static final Class<OptionBasicType>     OPTION_BASIC_TYPE      = OptionBasicType.class;
    public static final Class<Side>                SIDE                   = Side.class;
    public static final Class<OptionStyle>         OPTION_STYLE           = OptionStyle.class;
}
