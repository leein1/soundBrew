package com.soundbrew.soundbrew.repository.user.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.user.QUser;
import com.soundbrew.soundbrew.domain.user.QUserRole;
import com.soundbrew.soundbrew.domain.user.QUserSubscription;
import com.soundbrew.soundbrew.dto.*;
import com.soundbrew.soundbrew.dto.user.UserDTO;
import com.soundbrew.soundbrew.dto.user.UserDetailsDTO;
import com.soundbrew.soundbrew.dto.user.UserRoleDTO;
import com.soundbrew.soundbrew.dto.user.UserSubscriptionDTO;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class UserSearchRepositoryImpl implements UserSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;

    private final QUser user = QUser.user;
    private final QUserRole userRole = QUserRole.userRole;
    private final QUserSubscription userSubscription = QUserSubscription.userSubscription;

//    @Override
//    public UserDetailsDTO findUserDetailsById(int userId) {
//        QUser user = QUser.user;
//        QUserRole userRole = QUserRole.userRole;
//        QUserSubscription userSubscription = QUserSubscription.userSubscription;
//
//        return jpaQueryFactory.select(Projections.constructor(UserDetailsDTO.class,
//                        Projections.constructor(UserDTO.class,
//                                user.userId,
//                                user.subscriptionId,
//                                user.name,
//                                user.nickname,
//                                user.password,
//                                user.phoneNumber,
//                                user.email,
//                                user.emailVerified,
//                                user.creditBalance,
//                                user.profileImagePath,
//                                user.birth
//                        ),
//                        Projections.constructor(UserRoleDTO.class,
//                                userRole.id.roleId,
//                                userRole.id.userId
//                        ),
//                        Projections.constructor(UserSubscriptionDTO.class,
//                                userSubscription.userId,
//                                userSubscription.subscriptionId,
//                                userSubscription.firstBillingDate,
//                                userSubscription.nextBillingDate,
//                                userSubscription.paymentStatus
//
//                        )
//                ))
//                .from(user)
//                .join(userRole).on(user.userId.eq(userRole.id.userId))
//                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
//                .where(user.userId.eq(userId))
//                .fetchOne();
//    }

    @Override
    public Optional<UserDetailsDTO> findUserDetailsById(int userId) {

//        QUser user = QUser.user;
//        QUserRole userRole = QUserRole.userRole;
//        QUserSubscription userSubscription = QUserSubscription.userSubscription;

        return Optional.ofNullable(jpaQueryFactory.select(Projections.bean(UserDetailsDTO.class,
                        Projections.bean(UserDTO.class,
                                user.userId,
                                user.subscriptionId,
                                user.name,
                                user.nickname,
                                user.password,
                                user.phoneNumber,
                                user.email,
                                user.emailVerified,
                                user.creditBalance,
                                user.profileImagePath,
                                user.birth,
                                user.createDate, // BaseDTO의 필드
                                user.modifyDate  // BaseDTO의 필드

                        ).as("userDTO"),

                        Projections.bean(UserRoleDTO.class,
                                userRole.id.roleId,
                                userRole.id.userId
                        ).as("userRoleDTO"),

                        Projections.bean(UserSubscriptionDTO.class,
                                userSubscription.userId,
                                userSubscription.subscriptionId,
                                userSubscription.firstBillingDate,
                                userSubscription.nextBillingDate,
                                userSubscription.paymentStatus,
                                userSubscription.createDate,
                                userSubscription.modifyDate

                        ).as("userSubscriptionDTO")
                ))
                .from(user)
                .join(userRole).on(user.userId.eq(userRole.id.userId))
                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
                .where(user.userId.eq(userId))
                .fetchOne());
    }

    @Override
    public Optional<UserDetailsDTO> findUserDetailsByEmail(String email) {
        return Optional.ofNullable(jpaQueryFactory.select(Projections.bean(UserDetailsDTO.class,
                        Projections.bean(UserDTO.class,
                                user.userId,
                                user.subscriptionId,
                                user.name,
                                user.nickname,
                                user.password,
                                user.phoneNumber,
                                user.email,
                                user.emailVerified,
                                user.creditBalance,
                                user.profileImagePath,
                                user.birth,
                                user.createDate, // BaseDTO의 필드
                                user.modifyDate  // BaseDTO의 필드

                        ).as("userDTO"),

                        Projections.bean(UserRoleDTO.class,
                                userRole.id.roleId,
                                userRole.id.userId
                        ).as("userRoleDTO"),

                        Projections.bean(UserSubscriptionDTO.class,
                                userSubscription.userId,
                                userSubscription.subscriptionId,
                                userSubscription.firstBillingDate,
                                userSubscription.nextBillingDate,
                                userSubscription.paymentStatus,
                                userSubscription.createDate,
                                userSubscription.modifyDate

                        ).as("userSubscriptionDTO")
                ))
                .from(user)
                .join(userRole).on(user.userId.eq(userRole.id.userId))
                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
                .where(user.email.eq(email))
                .fetchOne());
    }

    @Override
    public Optional<Page<UserDetailsDTO>> findAllUserDetails(RequestDTO requestDTO) {
        // 검색 조건 처리
        BooleanBuilder builder = new BooleanBuilder();
        if (requestDTO.getType() != null) {
            List<String> types = List.of(requestDTO.getType());
            for (String type : types) {
                switch (type.toLowerCase()) {
                    case "n":
                        builder.and(user.nickname.contains(requestDTO.getKeyword()));
                        break;
                    // 필요시 다른 검색 조건 추가
                }
            }
        }

        // 정렬 조건을 별도 메서드에서 생성
        List<OrderSpecifier<?>> orderSpecifiers = createOrderSpecifiers(requestDTO);

        // QueryDSL을 사용한 데이터 조회 (distinct 추가 필요시 적용)
        List<UserDetailsDTO> userDetailsList = jpaQueryFactory
                .select(Projections.bean(UserDetailsDTO.class,
                        Projections.bean(UserDTO.class,
                                user.userId,
                                user.subscriptionId,
                                user.name,
                                user.nickname,
                                user.password,
                                user.phoneNumber,
                                user.email,
                                user.emailVerified,
                                user.creditBalance,
                                user.profileImagePath,
                                user.birth,
                                user.createDate,
                                user.modifyDate
                        ).as("userDTO"),

                        Projections.bean(UserRoleDTO.class,
                                userRole.id.roleId,
                                userRole.id.userId
                        ).as("userRoleDTO"),

                        Projections.bean(UserSubscriptionDTO.class,
                                userSubscription.userId,
                                userSubscription.subscriptionId,
                                userSubscription.firstBillingDate,
                                userSubscription.nextBillingDate,
                                userSubscription.paymentStatus,
                                userSubscription.createDate,
                                userSubscription.modifyDate
                        ).as("userSubscriptionDTO")
                ))
                .from(user)
                .join(userRole).on(user.userId.eq(userRole.id.userId))
                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
                .where(builder)
                .orderBy(orderSpecifiers.toArray(new OrderSpecifier<?>[0]))
                .offset(requestDTO.getPageable().getOffset())
                .limit(requestDTO.getPageable().getPageSize())
                .fetch();

        // 총 데이터 개수 조회 (필터 조건 적용)
        long total = jpaQueryFactory
                .select(user.countDistinct())
                .from(user)
                .join(userRole).on(user.userId.eq(userRole.id.userId))
                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
                .where(builder)
                .fetchOne();

        return Optional.of(new PageImpl<>(userDetailsList, requestDTO.getPageable(), total));
    }

    private List<OrderSpecifier<?>> createOrderSpecifiers(RequestDTO requestDTO) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        if (requestDTO.getMore() != null) {
            for (Map.Entry<String, String> entry : requestDTO.getMore().entrySet()) {
                String sortBy = entry.getKey();
                String sortDir = entry.getValue();  // 이미 String 타입
                OrderSpecifier<?> orderSpecifier;

                switch (sortBy) {
                    case "userId":
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? user.userId.asc()
                                : user.userId.desc();
                        break;
                    case "subscriptionId":
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? user.subscriptionId.asc()
                                : user.subscriptionId.desc();
                        break;
                    case "creditBalance":
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? user.creditBalance.asc()
                                : user.creditBalance.desc();
                        break;
                    case "paymentStatus": // Boolean 필드 정렬
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? new CaseBuilder()
                                .when(userSubscription.paymentStatus.eq("true"))
                                .then(1)
                                .otherwise(0)
                                .asc()
                                : new CaseBuilder()
                                .when(userSubscription.paymentStatus.eq("true"))
                                .then(1)
                                .otherwise(0)
                                .desc();
                        break;
                    case "roleId":
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? userRole.id.roleId.asc()
                                : userRole.id.roleId.desc();
                        break;
                    default: // 기본 정렬: userId
                        orderSpecifier = sortDir.equalsIgnoreCase("asc")
                                ? user.userId.asc()
                                : user.userId.desc();
                }

                orderSpecifiers.add(orderSpecifier);
            }
        }

        return orderSpecifiers;
    }
}




//    public Optional<Page<UserDetailsDTO>> findAllUserDetails(Pageable pageable) {
//
//        List<UserDetailsDTO> userDetailsList = jpaQueryFactory.select(
//                Projections.constructor(UserDetailsDTO.class,
//                        Projections.constructor(UserDTO.class,
//                                user.userId,
//                                user.subscriptionId,
//                                user.name,
//                                user.nickname,
//                                user.password,
//                                user.phoneNumber,
//                                user.email,
//                                user.emailVerified,
//                                user.creditBalance,
//                                user.profileImagePath,
//                                user.birth
//                        ),
//                        Projections.constructor(UserRoleDTO.class,
//                                userRole.id.roleId,
//                                userRole.id.userId
//                        ),
//                        Projections.constructor(UserSubscriptionDTO.class,
//                                userSubscription.userId,
//                                userSubscription.subscriptionId,
//                                userSubscription.firstBillingDate,
//                                userSubscription.nextBillingDate,
//                                userSubscription.paymentStatus
//                        )
//                ))
//                .from(user)
//                .join(userRole).on(user.userId.eq(userRole.id.userId))
//                .leftJoin(userSubscription).on(user.userId.eq(userSubscription.userId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        long total = jpaQueryFactory.select(user.count())
//                .from(user)
//                .fetchOne();
//
//        return Optional.of(new PageImpl<>(userDetailsList, pageable, total));
//    }



//    @Override
//    public UserDetailsDTO findUserDetailsById(int userId) {
//
//        QUser user = QUser.user;
//
//        UserDetailsDTO userDetailsDTO = jpaQueryFactory.selectFrom(user)
//                .
//
//
//
//
//        return null;
//
//    }






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
