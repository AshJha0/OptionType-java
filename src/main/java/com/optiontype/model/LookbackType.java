package com.optiontype.model;

public enum LookbackType {
    FIXED_STRIKE,
    FLOATING_STRIKE;

    @Override
    public String toString() {
        return switch (this) {
            case FIXED_STRIKE    -> "Fixed-Strike Lookback Option";
            case FLOATING_STRIKE -> "Floating-Strike Lookback Option";
        };
    }
}
