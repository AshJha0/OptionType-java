package com.optiontype.model;

public enum AsianAveragingType {
    ARITHMETIC,
    GEOMETRIC;

    public boolean isArithmetic() { return this == ARITHMETIC; }
    public boolean isGeometric()  { return this == GEOMETRIC; }

    @Override
    public String toString() {
        return switch (this) {
            case ARITHMETIC -> "Arithmetic Averaging";
            case GEOMETRIC  -> "Geometric Averaging";
        };
    }
}
