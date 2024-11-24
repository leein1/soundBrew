package com.soundbrew.soundbrew.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@ToString
@Builder
public class ResponseDTO<E> {

    private List<E> dtoList;

    private boolean hasContent;
}
