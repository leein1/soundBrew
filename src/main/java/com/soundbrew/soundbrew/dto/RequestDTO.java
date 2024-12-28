package com.soundbrew.soundbrew.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
<<<<<<< HEAD
import lombok.NoArgsConstructor;
=======
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
>>>>>>> feature/kyoung
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
<<<<<<< HEAD
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @Builder.Default
    int page = 1;

    @Builder.Default
    int size = 10;

    String keyword;

    Map<String,String> more;

    //  복수형 검색어 변수 추가 필요

    //  검색조건
    String type;

    String link;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
=======
import java.util.Map;

@Data
@SuperBuilder
@AllArgsConstructor
@RequiredArgsConstructor
public class RequestDTO {
    @Builder.Default
    private int page=1;
    @Builder.Default
    private int size =10;
    private String type;// n,t,c
    private String keyword;
    private String link;
    private Map<String,String> more;

    public String[] getType(){
        if (type==null || type.isEmpty()){
>>>>>>> feature/kyoung
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String... props){
<<<<<<< HEAD
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink(){

        if(link == null) {

            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

=======
        return PageRequest.of(this.page-1, this.size, Sort.by(props).descending());
    }

    public String getLink() {
        if (link == null) {
            StringBuilder builder = new StringBuilder();
            builder.append("page=" + this.page);
>>>>>>> feature/kyoung
            builder.append("&size=" + this.size);

            if (type != null && type.length() > 0) {
                builder.append("&type=" + type);
            }

            if (keyword != null) {
                try {
                    builder.append("&keyword=" + URLEncoder.encode(keyword, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
<<<<<<< HEAD
=======

//            if (more != null && !more.isEmpty()) {
//                more.forEach((key, value) -> {
//                    try {
//                        builder.append("&more%5B" + URLEncoder.encode(key, "UTF-8") + "%5D=" + URLEncoder.encode(value, "UTF-8"));
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }

>>>>>>> feature/kyoung
            link = builder.toString();
        }
        return link;
    }


}
