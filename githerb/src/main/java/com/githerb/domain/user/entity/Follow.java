package com.githerb.domain.user.entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {
    @Id
    @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User followingUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User followerUser;

    @Builder
    public Follow(User followingUser, User followerUser) {
        this.followingUser = followingUser;
        this.followerUser = followerUser;
    }

    public static Follow addFollowing(User loginUser, User followingUser) {
        Follow follow = Follow.builder()
                            .followingUser(followingUser)
                            .followerUser(loginUser)
                            .build();
        return follow;
    }

    @Override
    public String toString() {
        return "[USER]following:" + followingUser.getUsername() + "|follower:" + followerUser.getUsername();
    }
}
