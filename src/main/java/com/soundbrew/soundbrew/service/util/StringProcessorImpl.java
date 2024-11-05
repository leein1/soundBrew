package com.soundbrew.soundbrew.service.util;

import org.springframework.stereotype.Component;

@Component
public class StringProcessorImpl implements StringProcessor{

    @Override
    public String removeAllWhiteSpace(String str) {
        str = str.trim();

        if (str.isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        str = str.replace(" ", "");

        return str; // 최종 결과 반환
    }

    @Override
    public String removeAllWhiteSpaceWithLowerCase(String str) {
        str = str.trim();

        if (str.trim().isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        str = str.replace(" ", "").toLowerCase();

        return str;
    }

    @Override
    public String removeEndpointsWhiteSpace(String str) {
        str = str.trim();

        if (str.trim().isEmpty()) {
            return null;  // 필요에 따라 ""나 null 반환
        }

        return str;
    }
}
