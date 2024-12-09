package com.soundbrew.soundbrew.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Optional;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // null 값인 필드는 제외
public class ResponseDTO<E> {

    private String message;

    private List<E> dtoList;

    private boolean hasContent;

    private int page;
    private int size;
    private int total;

    //시작 페이지 번호
    private int start;

    //끝 페이지 번호
    private int end;

    //이전 페이지 존재 여부
    private boolean prev;

    //다음 페이지 존재 여부
    private boolean next;

//   페이징 추가 예정

    @Builder(builderMethodName = "withMessage")
    public ResponseDTO(String message){
        this.message = message;
    }

    @Builder(builderMethodName = "withAll")
    public ResponseDTO(RequestDTO requestDTO, List<E> dtoList, int total){

        if(total <= 0){
            return;
        }

        this.page = requestDTO.getPage();
        this.size = requestDTO.getSize();

        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page/10.0)) * 10;//화면에서의 마지막 번호

        this.start = this.end - 9;//화면에서의 시작 번호

        int last = (int)(Math.ceil((total/(double)size))); //데이터 개수를 계산한 마지막 페이지 번호

        this.end = end > last ? last : end;

        this.prev = this.start > 1;

        this.next = total > this.end * this.size;
    }


}
