package com.soundbrew.soundbrew.util.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.regex.Pattern;

public class TagListValidator implements ConstraintValidator<ValidTagList, List<String>> {
    private static final Pattern VALID_PATTERN = Pattern.compile("^[a-zA-Z가-힣0-9 ]+$");

    @Override
    public boolean isValid(List<String> tags, ConstraintValidatorContext context) {
        // 비어있거나 null이면 검증 통과
        if (tags == null || tags.isEmpty()) {
            return true; // 필수가 아니라면 null/빈 리스트 허용
        }

        for (String tag : tags) {
            if (tag == null || tag.length() < 2 || tag.length() > 50 || !VALID_PATTERN.matcher(tag).matches()) {
                return false;
            }
        }
        return true;
    }
}
