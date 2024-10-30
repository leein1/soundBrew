package com.soundbrew.soundbrew.util;

public interface StringValidator {

    boolean hasNoSpecialCharacters(String str);

    boolean isLengthWithinBounds(String str, int min, int max);

}
