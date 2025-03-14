package com.soundbrew.soundbrew.util.valid;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


//JavaDoc 문서를 사용할때 씀.
//Java 코드에서 자동으로 문서를 생성하는 기능. 즉, Java 코드를 기반으로 .html 형태의 API 문서를 만들 수 있다.
//여기서 API란 프로젝트 내에서 사용되는 클래스, 메서드, 필드 등의 정보.
@Documented
@Constraint(validatedBy = TagListValidator.class) // Validator 연결
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
// public "class" (...) 위치에 @interface가 들어가면, 일종의 인터페이스.
// 하지만 정확하게는, 어노테이션을 정의하는 인터페이스. == "@interface 키워드를 사용하여 새로운 애너테이션을 정의"
public @interface ValidTagList {
    String message() default "각 태그는 2~50자여야 하며, 특수문자를 포함할 수 없습니다?";

    //검증 그룹(Validation Groups)을 사용할때 씀.
    //검증 그룹을 사용하면 특정 상황에서만 유효성 검사를 수행할 수 있습니다.
    Class<?>[] groups() default {};

//    메타데이터를 전달하기 위한 용도
//    보통 검증 결과를 그룹화하거나, 추가적인 정보를 제공할 때 사용
    Class<? extends Payload>[] payload() default {};
}