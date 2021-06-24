package com.githerb.domain.plant.repository;
import com.githerb.domain.plant.entity.Plant;
import com.githerb.domain.user.entity.QUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.githerb.domain.plant.entity.QPlant.plant;
import static com.githerb.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class PlantQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Plant> findByIdJoinUser(Long plantId, String username) {
        Plant findPlant = queryFactory
                .selectFrom(plant)
                .join(plant.user, user).fetchJoin()
                .where(plant.id.eq(plantId), user.username.eq(username))
                .fetchOne();
        return Optional.ofNullable(findPlant);
    }
}
