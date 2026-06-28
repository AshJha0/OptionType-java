package com.optiontype.model;

public enum Side {
    CALL,
    PUT;

    public boolean isCall() { return this == CALL; }
    public boolean isPut()  { return this == PUT; }
}
