package com.soundbrew.soundbrew.repository.search;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.QUser;
import com.soundbrew.soundbrew.domain.User;
import com.soundbrew.soundbrew.dto.RequestDTO;
import com.soundbrew.soundbrew.dto.ResponseDTO;
import com.soundbrew.soundbrew.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.NamedStoredProcedureQueries;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserSearchRepositoryImpl implements UserSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;

    public ResponseDTO<UserDTO> searchTest() {
        QUser user = QUser.user;

        List<User> resultList = jpaQueryFactory.selectFrom(user)
                .where(user.nickname.contains("lee"))
                .fetch();

        List<UserDTO> userDTOList = resultList.stream()
                .map(u -> modelMapper.map(u, UserDTO.class))
                .collect(Collectors.toList());

        int totalCount = (int) jpaQueryFactory.selectFrom(user)
                .where(user.nickname.contains("lee"))
                .fetchCount();

        return ResponseDTO.<UserDTO>withAll()
                .dtoList(userDTOList)
                .total(totalCount)
                .build();
    }


}



//
////    public UserSearchRepositoryImpl(Class<?> domainClass) {
////        super(domainClass);
////    }
//    private final ModelMapper modelMapper;
//
//    public UserSearchRepositoryImpl() {
//        super(User.class);
//        modelMapper = new ModelMapper();
//    }
//
//    @Override
//    public ResponseDTO<UserDTO> searchTest() {
//
//        QUser user = QUser.user;    //Q도메인 객체 선언
//
//        JPQLQuery<User> query = from(user); //  select*from user
//
//        query.where(user.nickname.contains("lee")); // 닉네임에 lee를 포함하는 것
//
//        // 페치 결과 가져오기
//        List<User> resultList = query.fetch();
//
//        // DTO 변환
//        List<UserDTO> userDTOList = resultList.stream()
//                .map(userDomain -> modelMapper.map(userDomain,UserDTO.class)) // UserDTO의 생성자에 맞게 수정
//                .collect(Collectors.toList());
//
//        // 카운트 가져오기
//        int totalCount = (int)query.fetchCount();
//
//        // ResponseDTO 구성
//        ResponseDTO<UserDTO> responseDTO = ResponseDTO.<UserDTO>withAll()
//                .requestDto(new RequestDTO())
//                .dtoList(userDTOList)
//                .total(totalCount)
//                .build();
//
//        return responseDTO;
//    }
//}
