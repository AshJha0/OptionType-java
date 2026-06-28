package com.optiontype.model;

public enum BarrierType {
    UP_AND_IN,
    UP_AND_OUT,
    DOWN_AND_IN,
    DOWN_AND_OUT;

    public boolean isKnockIn()  { return this == UP_AND_IN  || this == DOWN_AND_IN; }
    public boolean isKnockOut() { return this == UP_AND_OUT || this == DOWN_AND_OUT; }
    public boolean isUp()       { return this == UP_AND_IN  || this == UP_AND_OUT; }
    public boolean isDown()     { return this == DOWN_AND_IN || this == DOWN_AND_OUT; }

    @Override
    public String toString() {
        return switch (this) {
            case UP_AND_IN   -> "Up-And-In Barrier";
            case UP_AND_OUT  -> "Up-And-Out Barrier";
            case DOWN_AND_IN -> "Down-And-In Barrier";
            case DOWN_AND_OUT -> "Down-And-Out Barrier";
        };
    }
}
