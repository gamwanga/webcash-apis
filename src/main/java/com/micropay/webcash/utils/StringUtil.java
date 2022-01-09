package com.micropay.webcash.utils;

public class StringUtil {
    public static String generateRandomString(int numberOfCharacters) {
        String AlphaNumericString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789";
        StringBuilder sb = new StringBuilder(numberOfCharacters);
        for (int i = 0; i < numberOfCharacters; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
