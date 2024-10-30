package com.soundbrew.soundbrew.util;

public class UserValidator extends StringValidatorImpl{

    //    비밀번호 입력 형태 검사
    //    특수문자 1개 이상, 대문자 1개이상, 숫자 1개이상 만족해야 함
    public  boolean isPasswordFormatValid(String str) {

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
