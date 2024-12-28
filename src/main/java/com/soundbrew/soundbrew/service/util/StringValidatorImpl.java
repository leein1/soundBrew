package com.soundbrew.soundbrew.service.util;

public class StringValidatorImpl implements StringValidator{

    @Override
    public boolean hasNoSpecialCharacters(String str) {
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

        return true;    }

    @Override
    public boolean isLengthWithinBounds(String str, int min, int max) {
        //  조건 논의 필요
        //  min보다 작거나, max보다 크면 false
        //  나머지 조건은 min보다 같거나 크고, max보다 같거나 작으므로 전부 만족
        if(str.length() < min || str.length() > max){
            str.trim();
            return false;
        }

        return true;    }
}
