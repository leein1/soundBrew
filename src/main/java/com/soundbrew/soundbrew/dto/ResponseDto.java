package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDto<E> {
    private List<E> dto;    // 데이터 목록

    private int page;       // 현재 페이지
    private int size;       // 페이지 크기
    private int total;      // 전체 데이터 개수
    private int start;      // 시작 페이지
    private int end;        // 끝 페이지
    private boolean prev;   // 이전 페이지 존재 여부
    private boolean next;   // 다음 페이지 존재 여부
    private String keyword1; // 검색 키워드

    public ResponseDto(RequestDto requestDto, List<E> dto, int total){
        if(total <= 0) return;
        this.keyword1 = requestDto.getKeyword();
        this.page = requestDto.getPage();
        this.size = requestDto.getSize();
        this.total = total;
        this.dto = dto;
        this.end = (int)(Math.ceil(this.page/10.0))*10; // 화면에서 마지막 번호
        this.start = this.end - 9; // 화면에서 시작 번호
        int last = (int)(Math.ceil(total/(double)size));
        this.end = end > last ? last : end;
        this.prev = this.page > 1;
//        this.prev = this.start > 1;
//        this.next = total > this.end * this.size;
        this.next = this.page < Math.ceil((double) total / this.size);
    }
}
