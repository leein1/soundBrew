package com.soundbrew.soundbrew.service.util;

public class FilteringUtil {

    // 들어올 정보가 앨범, 음악

    // 앨범 - 낫 널, 앞 뒤, +대문자
    // 음악 - 낫 널, 앞 뒤, +대문자
    // 닉네임 - 낫 널, 앞 뒤, +대문자

    // 휴대폰 - 낫 널, 앞 뒤 중간
    // 이메일 - 낫 널, 앞 뒤 중간, +소문자
    // 유저 - 낫 널, 앞 뒤 중간, +대문자
    // 비밀번호 - 낫 널, 앞 뒤 중간, +대문자

    // *** 입력 ***
    // 즉, 대 소문자 구별하는 이유는 -> 이상하게 입력하면, 올바르게 저장할려고.
    //                          -> 평범하게 검색하고, 의도대로 결과를 받을려고.

    public static String filteringWord(String before, String service) {
        // 공통 처리: 앞뒤 공백 제거
        before = before.trim();

        if (before == null || before.trim().isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        // 항목별 맞춤 처리
        String type = service; // 앨범, 음악, 유저, 닉네임 등 항목의 타입에 따라 달라짐

        switch (type) {
            case "유저":
            case "비밀번호":
                before = before.replace(" ", "");
                break;

            case "이메일":
            case "휴대폰":
                before = before.replace(" ", "").toLowerCase();
                break;
        }
        return before; // 최종 결과 반환
    }

    public static String filteringWord2(){

    }

}
