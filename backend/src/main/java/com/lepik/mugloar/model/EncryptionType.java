package com.lepik.mugloar.model;

public enum EncryptionType {
    NONE(0),
    BASE64(1),
    ROT13(2);

    private final int code;

    EncryptionType(int code) {
        this.code = code;
    }

    public static EncryptionType fromCode(int code) {
        for (EncryptionType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        return NONE;
    }
}

