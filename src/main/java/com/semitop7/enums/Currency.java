package com.semitop7.enums;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum Currency {
    @JsonEnumDefaultValue
    NONE,
    GBP,
    USD,
    EUR,
    UAH,
    RUB;

    public String value() {
        return name();
    }

    public static Currency fromValue(String v) {
        return valueOf(v);
    }
}