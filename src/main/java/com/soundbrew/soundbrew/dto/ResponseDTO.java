package com.soundbrew.soundbrew.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

<<<<<<< HEAD
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // null 값인 필드는 제외
@ToString
public class ResponseDTO<E> {
    private E dto;
    private List<E> dtoList;    // 데이터 목록

=======
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_DEFAULT) // null 값인 필드는 제외
public class ResponseDTO<E> {
    private List<E> dtoList;// 데이터 목록
    private E dto; //단수일때 쓰는것.
>>>>>>> feature/kyoung
    private int page;       // 현재 페이지
    private int size;       // 페이지 크기
    private int total;      // 전체 데이터 개수
    private int start;      // 시작 페이지
    private int end;        // 끝 페이지
    private boolean prev;   // 이전 페이지 존재 여부
    private boolean next;   // 다음 페이지 존재 여부
<<<<<<< HEAD
    private String keyword; // 검색 키워드
    private String message;

    @Builder(builderMethodName = "withMessage")
    public ResponseDTO(String message) {
        this.message = message;
    }

    @Builder(builderMethodName = "withSingeData")
    public ResponseDTO(E dto) { // 단수형 데이터 생성자
        this.dto = dto;

    }

//    @Builder(builderMethodName = "withAll")
//    public ResponseDTO(RequestDTO requestDTO, List<E> dtoList, int total) {
////        if (total <= 0) return;
//        this.keyword = requestDTO.getKeyword();
//        this.page = requestDTO.getPage();
//        this.size = requestDTO.getSize();
//        this.total = total;
//        this.dtoList = dtoList;
//        this.end = (int) (Math.ceil(this.page / 10.0)) * 10; // 화면에서 마지막 번호
//        this.start = this.end - 9; // 화면에서 시작 번호
//        int last = (int) (Math.ceil(total / (double) size));
//        this.end = end > last ? last : end;
//        this.prev = this.page > 1;
////        this.prev = this.start > 1;
////        this.next = total > this.end * this.size;
//        this.next = this.page < Math.ceil((double) total / this.size);
//    }

    public static <E> ResponseDTO<E> withAll(RequestDTO requestDTO, List<E> dtoList, int total) {
        ResponseDTO<E> responseDTO = new ResponseDTO<>();

        responseDTO.keyword = requestDTO.getKeyword();
        responseDTO.page = requestDTO.getPage();
        responseDTO.size = requestDTO.getSize();
        responseDTO.total = total;
        responseDTO.dtoList = dtoList;

        if (total > 0) {
            // 마지막 페이지 계산
            responseDTO.end = (int) Math.ceil((double) responseDTO.page / 10) * 10;
            responseDTO.start = responseDTO.end - 9;

            // 전체 페이지 수 계산
            int last = (int) Math.ceil((double) total / responseDTO.size);

            // 마지막 번호 조정
            responseDTO.end = Math.min(responseDTO.end, last);
            responseDTO.prev = responseDTO.page > 1;
            responseDTO.next = responseDTO.page < last;
        } else {
            // 기본값 설정
            responseDTO.start = 0;
            responseDTO.end = 0;
            responseDTO.prev = false;
            responseDTO.next = false;
        }

        return responseDTO;
    }


}
=======
    private String keyword1; // 검색 키워드
    private String message;

    @Builder(builderMethodName = "withMessage")
    public ResponseDTO(String message){
        this.message = message;
    }

    @Builder(builderMethodName = "withSingleData")
    public ResponseDTO(E dto){
        this.dto = dto;
    }

    @Builder(builderMethodName = "withAll")
    public ResponseDTO(RequestDTO requestDTO, List<E> dtoList, int total){
        if(total <= 0) return;
        this.keyword1 = requestDTO.getKeyword();
        this.page = requestDTO.getPage();
        this.size = requestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;
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
>>>>>>> feature/kyoung
