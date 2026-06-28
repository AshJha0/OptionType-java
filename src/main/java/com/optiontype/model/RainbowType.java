package com.optiontype.model;

public enum RainbowType {
    BEST_OF,
    WORST_OF;

    public boolean isBestOf()  { return this == BEST_OF; }
    public boolean isWorstOf() { return this == WORST_OF; }
}
