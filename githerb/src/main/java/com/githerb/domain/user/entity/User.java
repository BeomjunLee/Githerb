package com.githerb.domain.user.entity;

import com.githerb.domain.plant.entity.Plant;
import com.githerb.domain.user.type.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String username;
    private String name;
    private String job;
    private String desc;
    private String tier;
    private String picture;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "followerUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followings = new ArrayList<>();

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "followingUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Follow> followers = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Plant> plants = new ArrayList<>();

    @Builder
    public User(String username, String name, String picture, UserRole userRole) {
        this.username = username;
        this.name = name;
        this.picture = picture;
        this.userRole = userRole;
    }

    public User update(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.picture = user.getPicture();
        return this;
    }
}
