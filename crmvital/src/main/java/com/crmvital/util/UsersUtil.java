package com.crmvital.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class UsersUtil {
    private static final String valid = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static final SecureRandom random = new SecureRandom();

    private UsersUtil() {
    }

    public  String CreatePassword(int length) {
        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(valid.length());
            result.append(valid.charAt(index));
        }
        return result.toString();
    }

    public static String generateUsername(String name, String firstLastName) {
        String base = ("" + name.charAt(0) + firstLastName).replaceAll("\\s", "").toLowerCase();
        int randomNum = (int)(Math.random() * 1000);
        return base + randomNum;
    }



}
