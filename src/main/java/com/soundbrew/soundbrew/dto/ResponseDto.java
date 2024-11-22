package com.soundbrew.soundbrew.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
public class ResponseDto<E> {
    private List<E> dto;
    private Pageable pageable;
    private String keyword1;

}
