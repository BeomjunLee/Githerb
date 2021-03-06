package com.githerb.domain.user.repository;
import com.githerb.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import static com.githerb.domain.plant.entity.QPlant.*;
import static com.githerb.domain.user.entity.QUser.*;

@Repository
@RequiredArgsConstructor
public class UserQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<User> findByIdJoinPlant(Long id) {
        User findUser = queryFactory
                .selectDistinct(user)
                .from(user)
                .leftJoin(user.plants, plant).fetchJoin()
                .where(user.id.eq(id))
                .fetchOne();
        return Optional.ofNullable(findUser);
    }
}
