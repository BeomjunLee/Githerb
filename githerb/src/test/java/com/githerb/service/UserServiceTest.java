package com.githerb.service;
import com.githerb.domain.plant.entity.Plant;
import com.githerb.domain.plant.repository.PlantRepository;
import com.githerb.domain.plant.type.PlantStatus;
import com.githerb.domain.user.dto.response.ResponseUserInfo;
import com.githerb.domain.user.entity.Follow;
import com.githerb.domain.user.entity.User;
import com.githerb.domain.user.repository.FollowRepository;
import com.githerb.domain.user.repository.UserRepository;
import com.githerb.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FollowRepository followRepository;
    @Autowired
    EntityManager em;
    @Autowired
    PlantRepository plantRepository;

    List<User> users;

    @BeforeEach
    public void init() {
        users = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
        User user = User.builder()
                .username("test"+i)
                .name("이름"+i)
                .picture("사진경로"+i)
                .build();
            users.add(userRepository.save(user));
        }

        followRepository.save(Follow.addFollowing(users.get(0), users.get(1))); //test1 팔로잉 test2
        followRepository.save(Follow.addFollowing(users.get(0), users.get(2))); //test1 팔로잉 test3
        followRepository.save(Follow.addFollowing(users.get(0), users.get(3))); //test1 팔로잉 test4

        followRepository.save(Follow.addFollowing(users.get(1), users.get(0))); //test2 팔로잉 test1
        followRepository.save(Follow.addFollowing(users.get(2), users.get(0))); //test3 팔로잉 test1
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("팔로잉, 팔로워 조회 테스트")
    public void follow() throws Exception{
        //given when
        List<Follow> followings = followRepository.findAllByFollowerUser_Username("test1"); // -> test1 의 팔로잉 = test2, test3, test4
        List<Follow> followers = followRepository.findAllByFollowingUser_Username("test1"); // -> test1 의 팔로워 = test2, test3

        //then
        // -> test1 의 팔로잉 = test2, test3, test4
        assertThat(followings.size()).isEqualTo(3);
        assertThat(followings.get(0).getFollowingUser().getUsername()).isEqualTo("test2");
        assertThat(followings.get(1).getFollowingUser().getUsername()).isEqualTo("test3");
        assertThat(followings.get(2).getFollowingUser().getUsername()).isEqualTo("test4");

        // -> test1 의 팔로워 = test2, test3
        assertThat(followers.size()).isEqualTo(2);
        assertThat(followers.get(0).getFollowerUser().getUsername()).isEqualTo("test2");
        assertThat(followers.get(1).getFollowerUser().getUsername()).isEqualTo("test3");
    }
    
    @Test
    @DisplayName("회원 조회 테스트")
    public void getUserInfo() throws Exception{
        //given
        Plant plantInProgress1 = Plant.builder().plantStatus(PlantStatus.IN_PROGRESS).build();
        Plant plantInProgress2 = Plant.builder().plantStatus(PlantStatus.IN_PROGRESS).build();
        plantInProgress1.setUser(users.get(0));
        plantInProgress2.setUser(users.get(0));
        Plant plantDone = Plant.builder().plantStatus(PlantStatus.DONE).build();
        plantDone.setUser(users.get(0));
        Plant plantFailed = Plant.builder().plantStatus(PlantStatus.FAILED).build();
        plantFailed.setUser(users.get(0));

        plantRepository.save(plantInProgress1);
        plantRepository.save(plantInProgress2);
        plantRepository.save(plantDone);
        plantRepository.save(plantFailed);

        //when
        ResponseUserInfo responseUserInfo = userService.getUserInfo(users.get(0).getId());

        //then
        assertThat(responseUserInfo).extracting("id", "username", "name", "picture",
                "userJob", "userDesc", "userTier", "following", "follower",
                "plantAchieve.all", "plantAchieve.inProgress", "plantAchieve.done", "plantAchieve.failed")
                .containsExactly(users.get(0).getId(), "test1", "이름1", "사진경로1",
                        null, null, null, 3, 2,
                        4, 2, 1, 1);
    }

    @Test
    @DisplayName("회원 전체 조회 테스트 (batchsize)")
    public void findALlBatchSize() throws Exception{
        //given when
        List<User> users = userRepository.findAll();

        //then
        assertThat(users.get(0).getFollowings().size()).isEqualTo(3);
        assertThat(users.get(0).getFollowers().size()).isEqualTo(2);
        assertThat(users.get(1).getFollowings().size()).isEqualTo(1);
        assertThat(users.get(1).getFollowers().size()).isEqualTo(1);
        assertThat(users.get(2).getFollowings().size()).isEqualTo(1);
        assertThat(users.get(2).getFollowers().size()).isEqualTo(1);
        assertThat(users.get(3).getFollowings().size()).isEqualTo(0);
        assertThat(users.get(3).getFollowers().size()).isEqualTo(1);


    }
}