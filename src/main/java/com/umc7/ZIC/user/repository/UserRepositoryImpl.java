package com.umc7.ZIC.user.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.umc7.ZIC.user.domain.QUser;
import com.umc7.ZIC.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final QUser user = QUser.user;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserResponseDto.user findUserByEmail(String email) {
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(user.email.eq(email));

        return jpaQueryFactory
                .select(Projections.constructor(UserResponseDto.user.class,
                        user.id,
                        user.email,
                        user.name,
                        user.kakaoId,
                        user.region))
                .from(user)
                .where(predicate)
                .fetchOne();
    }
}
