package com.soundbrew.soundbrew.repository.search;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.soundbrew.soundbrew.domain.QUser;
import com.soundbrew.soundbrew.domain.QUserRole;
import com.soundbrew.soundbrew.domain.QUserSubscription;
import com.soundbrew.soundbrew.dto.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.NamedStoredProcedureQueries;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class UserSearchRepositoryImpl implements UserSearchRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private final ModelMapper modelMapper;

    private final QUser user = QUser.user;
    private final QUserRole userRole = QUserRole.userRole;
    private final QUserSubscription userSubscription = QUserSubscription.userSubscription;

//    public ResponseDTO<UserDTO> searchTest() {
//        QUser user = QUser.user;
//
//        List<User> resultList = jpaQueryFactory.selectFrom(user)
//                .where(user.nickname.contains("lee"))
//                .fetch();
//
//        List<UserDTO> userDTOList = resultList.stream()
//                .map(u -> modelMapper.map(u, UserDTO.class))
//                .collect(Collectors.toList());
//
//        int totalCount = (int) jpaQueryFactory.selectFrom(user)
//                .where(user.nickname.contains("lee"))
//                .fetchCount();
//
//        return ResponseDTO.<UserDTO>withAll()
//                .dtoList(userDTOList)
//                .total(totalCount)
//                .build();
//    }

    @Override
    public ResponseDTO<UserDTO> searchTest() {
        return null;
    }

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
    public Optional<Page<UserDetailsDTO>> findAllUserDetails(RequestDTO requestDTO) {

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
                .offset(requestDTO.getPageable().getOffset())
                .limit(requestDTO.getPageable().getPageSize())
                .fetch();

        long total = jpaQueryFactory.select(user.count())
                .from(user)
                .fetchOne();

        return Optional.of(new PageImpl<>(userDetailsList, requestDTO.getPageable(), total));
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
}


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
