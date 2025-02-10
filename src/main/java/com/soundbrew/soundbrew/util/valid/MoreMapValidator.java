package com.soundbrew.soundbrew.util.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;


public class MoreMapValidator implements ConstraintValidator<ValidMoreMap, Map<String, String>> {
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9 ]+$");

    @Override
    public boolean isValid(Map<String, String> more, ConstraintValidatorContext context) {
        if (more == null || more.isEmpty()) {
            return true;  // 빈 맵은 유효하다고 간주 (필수 입력이 아니라면)
        }

        // 각 항목에 대해 키와 값을 검사
        for (Map.Entry<String, String> entry : more.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // 키는 instrument, genre, mood만 허용 (예시)
            if (!key.equals("instrument") && !key.equals("genre") && !key.equals("mood")) {
                return false; // 허용되지 않은 키면 false 반환
            }

            // 값은 2~50자 사이여야 하며,
            // 대문자 및 특수문자는 허용하지 않고, 소문자, 한글, 숫자, 공백만 허용합니다.
            if (value == null || value.length() < 2 || value.length() > 50 || !VALID_PATTERN.matcher(value).matches()) {
                return false; // 값이 유효하지 않으면 false 반환
            }
        }

        return true;
    }
}