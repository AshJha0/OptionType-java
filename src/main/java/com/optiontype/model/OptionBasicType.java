package com.optiontype.model;

public record OptionBasicType(Side side, OptionStyle style) {

    public boolean isCall() { return side == Side.CALL; }
    public boolean isPut()  { return side == Side.PUT; }
}
