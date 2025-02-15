package com.soundbrew.soundbrew.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PasswordGenerator {

    // 사용할 문자 집합
    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()-_=+[]{}|;:',.<>/?";

    /**
     * 최소 8자리, 대문자 1개 이상, 특수문자 1개 이상, 숫자 1개
     *
     * throws IllegalArgumentException length가 8 미만인 경우
     */

    public static String generatePassword(int length) {

        if (length < 8) {
            throw new IllegalArgumentException("발급하는 비밀번호는 최소 8자리 이상이어야 함");
        }

        SecureRandom random = new SecureRandom();
        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        String allAllowed = UPPER + LOWER + DIGITS + SPECIAL;
        for (int i = 3; i < length; i++) {
            passwordChars.add(allAllowed.charAt(random.nextInt(allAllowed.length())));
        }

        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }

}
