package com.soundbrew.soundbrew.service.util;

public interface StringValidator {

    boolean hasNoSpecialCharacters(String str);

    boolean isLengthWithinBounds(String str, int min, int max);

}
