package com.vietqradminbe.infrastructure.configuration.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

@Slf4j
public class BcryptKeyUtil {
    public static String hashKeyActive(String keyValue, String secretKey, int duration) {
        String result = "";
        try {
            String plainText =duration + keyValue + secretKey;
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            result = sb.toString();
        } catch (Exception e) {
            log.error("hashKeyActive ERROR: " + e.toString());
        }
        return result;
    }
}

