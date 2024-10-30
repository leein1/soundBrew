package com.soundbrew.soundbrew.service.util;

public class FilteringUtil {

    public static String removeAllWhiteSpace(String str) {
        str = str.trim();

        if (str.isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }
        str = str.replace(" ", "");

        return str; // 최종 결과 반환
    }

    public static String removeAllWhiteSpaceWithLowerCase(String str){
        str = str.trim();

        if (str.isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        str = str.replace(" ", "").toLowerCase();

        return str;
    }

    public static String removeEndpointsWhiteSpace(String str){
        str = str.trim();

        if (str.isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        return str;
    }

    //    특수문자 제한
    //    특수문자가 없으면 true, 있으면 false 반환
    public static boolean hasNoSpecialCharacters(String str) {

        //  특수문자가 포함되어 있는지 확인
        //  문자 또는 숫자도 아니고, 공백도 아닐때 false
        for(char ch : str.toCharArray()){

            //  숫자나 문자면 false , 공백이면 false and 연산
            //  둘 다 true인 경우 -> 숫자도 문자 아님(true) && 공백 아님(true) = 특수문자
            //  그럼 if문 조건 만족으로 false 리턴

            if(!Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)){
                return false;
            }
        }

        return true;
    }

    //    길이 제한 하한,상한
    public static boolean isLengthWithinBounds(String str, int min, int max) {

        //  조건 논의 필요
        //  min보다 작거나, max보다 크면 false
        //  나머지 조건은 min보다 같거나 크고, max보다 같거나 작으므로 전부 만족
        if(str.length() < min || str.length() > max){
            str.trim();
            return false;
        }

        return true;
    }

    //    비밀번호 입력 형태 검사
    //    특수문자 1개 이상, 대문자 1개이상, 숫자 1개이상 만족해야 함
    public static boolean isPasswordFormatValid(String str) {

        //  boolean 타입 기본값 false 특수문자, 대문자, 숫자
        boolean hasSpecialCharacters = false;
        boolean hasUpperCase = false;
        boolean hasDigit = false;

        for(char ch : str.toCharArray()){

            //  특수문자가 있다면 true
            if(!hasSpecialCharacters &&  !Character.isLetterOrDigit(ch) && !Character.isWhitespace(ch)){
                hasSpecialCharacters = true;
            }

            //  대문자가 있다면 true
            if(!hasUpperCase && Character.isUpperCase(ch)){
                hasUpperCase = true;
            }

            //  숫자가 있다면 true
            if(!hasDigit && Character.isDigit(ch)){
                hasDigit = true;
            }

            if(hasSpecialCharacters && hasUpperCase && hasDigit){
                return true;
            }
        }

        return false;
    }

}
