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
    private static final String SPECIAL = "!@#$&?";

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

        // 필수 요소 먼저 추가
        passwordChars.add(UPPER.charAt(random.nextInt(UPPER.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));
        passwordChars.add(SPECIAL.charAt(random.nextInt(SPECIAL.length())));

        String allAllowed = UPPER + LOWER + DIGITS + SPECIAL;

        // i는 passwordChars 인덱스에 해당!! 헷갈리면 안됨
        for (int i = 3; i < length; i++) {
            passwordChars.add(allAllowed.charAt(random.nextInt(allAllowed.length())));
        }

        // 필수 요소가 맨 앞에 존재하는것을 방지하기 위해 shuffle
        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }

}
