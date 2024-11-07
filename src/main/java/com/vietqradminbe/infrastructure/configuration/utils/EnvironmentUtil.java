package com.vietqradminbe.infrastructure.configuration.utils;

public class EnvironmentUtil {
    private static final int LENGTH_KEY_ACTIVE_BANK = 12;
    private static final String CHARACTERS_KEY_ACTIVE_BANK = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static int getLengthKeyActiveBank() {
        return LENGTH_KEY_ACTIVE_BANK;
    }

    public static String getCharactersKeyActiveBank() {
        return CHARACTERS_KEY_ACTIVE_BANK;
    }
}

