package com.soundbrew.soundbrew.dto;

import lombok.*;


import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Positive;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDTO {

    @Builder.Default
    @Positive
    int page = 1;

    @Builder.Default
    @Positive
    int size = 10;

    String keyword;

//    @ValidMoreMap
    Map<String,String> more;

    //  복수형 검색어 변수 추가 필요

    //  검색조건
    String type;

    String link;

    public String[] getTypes(){
        if(type == null || type.isEmpty()){
            return null;
        }
        return type.split("");
    }

    public Pageable getPageable(String... props){
        return PageRequest.of(this.page -1, this.size, Sort.by(props).descending());
    }

    public String getLink(){

        if(link == null) {

            StringBuilder builder = new StringBuilder();

            builder.append("page=" + this.page);

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
            link = builder.toString();
        }
        return link;
    }


}
