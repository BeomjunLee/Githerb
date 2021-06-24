package com.githerb.domain.user.repository;

import com.githerb.domain.user.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    List<Follow> findAllByFollowerUser_Username(String username);
    List<Follow> findAllByFollowingUser_Username(String username);
    Optional<Follow> findByFollowerUser_IdAndFollowingUser_Id(Long loginUserId, Long followingUserId);
}
