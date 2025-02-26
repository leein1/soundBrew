package com.soundbrew.soundbrew.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // null 값인 필드는 제외
@ToString
public class ExceptionResponseDTO {
    private int status;          // HTTP 상태 코드
    private String message;        // 에러 메시지 (있을 경우)
    private String error; // 지정한 enum 에러 키워드
}
