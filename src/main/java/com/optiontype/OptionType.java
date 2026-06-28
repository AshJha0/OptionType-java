package com.optiontype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.optiontype.model.AsianAveragingType;
import com.optiontype.model.BarrierType;
import com.optiontype.model.BinaryType;
import com.optiontype.model.LookbackType;
import com.optiontype.model.RainbowType;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = OptionType.European.class,   name = "European"),
    @JsonSubTypes.Type(value = OptionType.American.class,   name = "American"),
    @JsonSubTypes.Type(value = OptionType.Bermuda.class,    name = "Bermuda"),
    @JsonSubTypes.Type(value = OptionType.Asian.class,      name = "Asian"),
    @JsonSubTypes.Type(value = OptionType.Barrier.class,    name = "Barrier"),
    @JsonSubTypes.Type(value = OptionType.Binary.class,     name = "Binary"),
    @JsonSubTypes.Type(value = OptionType.Lookback.class,   name = "Lookback"),
    @JsonSubTypes.Type(value = OptionType.Chooser.class,    name = "Chooser"),
    @JsonSubTypes.Type(value = OptionType.Cliquet.class,    name = "Cliquet"),
    @JsonSubTypes.Type(value = OptionType.Rainbow.class,    name = "Rainbow"),
    @JsonSubTypes.Type(value = OptionType.Spread.class,     name = "Spread"),
    @JsonSubTypes.Type(value = OptionType.Quanto.class,     name = "Quanto"),
    @JsonSubTypes.Type(value = OptionType.Exchange.class,   name = "Exchange"),
    @JsonSubTypes.Type(value = OptionType.Power.class,      name = "Power"),
    @JsonSubTypes.Type(value = OptionType.Compound.class,   name = "Compound"),
})
public sealed interface OptionType
        permits OptionType.European, OptionType.American, OptionType.Bermuda,
                OptionType.Asian, OptionType.Barrier, OptionType.Binary,
                OptionType.Lookback, OptionType.Compound, OptionType.Chooser,
                OptionType.Cliquet, OptionType.Rainbow, OptionType.Spread,
                OptionType.Quanto, OptionType.Exchange, OptionType.Power {

    static OptionType defaultValue() { return new European(); }

    default boolean isEuropean()     { return this instanceof European; }
    default boolean isAmerican()     { return this instanceof American; }
    default boolean isExotic()       { return !(this instanceof European) && !(this instanceof American); }

    default boolean isPathDependent() {
        return this instanceof Asian
            || this instanceof Barrier
            || this instanceof Lookback
            || this instanceof Cliquet;
    }

    default boolean isMultiAsset() {
        return this instanceof Rainbow
            || this instanceof Spread
            || this instanceof Exchange;
    }

    default boolean isKnockIn() {
        return this instanceof Barrier b && b.barrierType().isKnockIn();
    }

    default boolean isKnockOut() {
        return this instanceof Barrier b && b.barrierType().isKnockOut();
    }

    String displayName();

    record European() implements OptionType {
        public String displayName() { return "European Option"; }
    }

    record American() implements OptionType {
        public String displayName() { return "American Option"; }
    }

    record Bermuda(List<Double> exerciseDates) implements OptionType {
        public String displayName() {
            return "Bermuda Option (Exercise Dates: " + exerciseDates + ")";
        }
    }

    record Asian(AsianAveragingType averagingType) implements OptionType {
        public String displayName() {
            return "Asian Option (Averaging Type: " + averagingType + ")";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    record Barrier(BarrierType barrierType, double barrierLevel, Double rebate) implements OptionType {
        public String displayName() {
            return "Barrier Option (Type: " + barrierType + ", Level: " + barrierLevel + ", Rebate: " + rebate + ")";
        }
        public boolean hasRebate() { return rebate != null; }
    }

    record Binary(BinaryType binaryType) implements OptionType {
        public String displayName() {
            return "Binary Option (Type: " + binaryType + ")";
        }
    }

    record Lookback(LookbackType lookbackType) implements OptionType {
        public String displayName() {
            return "Lookback Option (Type: " + lookbackType + ")";
        }
    }

    record Chooser(double chooseDate) implements OptionType {
        public String displayName() {
            return "Chooser Option (Choice Date: " + chooseDate + ")";
        }
    }

    record Cliquet(List<Double> resetDates) implements OptionType {
        public String displayName() {
            return "Cliquet Option (Reset Dates: " + resetDates + ")";
        }
    }

    record Rainbow(RainbowType rainbowType, int numAssets) implements OptionType {
        public String displayName() {
            return "Rainbow Option (Type: " + rainbowType + ", Number of Assets: " + numAssets + ")";
        }
    }

    record Spread(double secondAsset) implements OptionType {
        public String displayName() {
            return "Spread Option (Second Asset: " + secondAsset + ")";
        }
    }

    record Quanto(double exchangeRate) implements OptionType {
        public String displayName() {
            return "Quanto Option (Exchange Rate: " + exchangeRate + ")";
        }
    }

    record Exchange(double secondAsset) implements OptionType {
        public String displayName() {
            return "Exchange Option (Second Asset: " + secondAsset + ")";
        }
    }

    record Power(double exponent) implements OptionType {
        public String displayName() {
            return "Power Option (Exponent: " + exponent + ")";
        }
    }

    final class Compound implements OptionType {
        private final OptionType underlyingOption;

        public Compound(OptionType underlyingOption) {
            this.underlyingOption = underlyingOption;
        }

        public OptionType underlyingOption() { return underlyingOption; }

        public String displayName() {
            return "Compound Option (Underlying: " + underlyingOption.displayName() + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Compound c)) return false;
            return underlyingOption.equals(c.underlyingOption);
        }

        @Override
        public int hashCode() { return underlyingOption.hashCode(); }

        @Override
        public String toString() { return displayName(); }
    }
}
