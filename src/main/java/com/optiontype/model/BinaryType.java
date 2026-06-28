package com.optiontype.model;

public enum BinaryType {
    CASH_OR_NOTHING,
    ASSET_OR_NOTHING,
    GAP;

    @Override
    public String toString() {
        return switch (this) {
            case CASH_OR_NOTHING  -> "Cash-Or-Nothing Binary Option";
            case ASSET_OR_NOTHING -> "Asset-Or-Nothing Binary Option";
            case GAP              -> "Gap Binary Option";
        };
    }
}
