package com.githerb.domain.user.service;

import com.githerb.domain.user.entity.Follow;
import com.githerb.domain.user.entity.User;
import com.githerb.domain.user.dto.UserDto;
import com.githerb.global.dto.ResponseResult;
import com.githerb.domain.user.repository.FollowRepository;
import com.githerb.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import static com.githerb.global.type.ResultType.RESULT_FOLLOW;
import static com.githerb.global.type.ResultType.RESULT_UN_FOLLOW;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    //회원 수정

    //회원 조회

    /**
     * 팔로잉 및 언팔로우 하기
     * @param username 사용자 아이디
     * @param followUserId 팔로우할 사용자 id
     * @return 응답 dto
     */
    public ResponseResult addFollowing(String username, Long followUserId) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        User followingUser = userRepository.findById(followUserId).orElseThrow(() -> new IllegalArgumentException("팔로우 회원을 찾을 수 없습니다"));
        Optional<Follow> follow = followRepository.findByFollowerUser_IdAndFollowingUser_Id(user.getId(), followUserId);

        if (follow.isEmpty()) {
            followRepository.save(Follow.addFollowing(user, followingUser));
            return ResponseResult.builder()
                    .status(RESULT_FOLLOW.getStatus())
                    .message(RESULT_FOLLOW.getMessage())
                    .build();
        }

        followRepository.deleteById(follow.get().getId());
        return ResponseResult.builder()
                .status(RESULT_UN_FOLLOW.getStatus())
                .message(RESULT_FOLLOW.getMessage())
                .build();
    }

    /**
     * 팔로잉 목록 보기
     * @param username 사용자 아이디
     * @return 내가 팔로잉 중인 User dto 리스트
     */
    //TODO 페이징
    public List<UserDto> getFollowingList(String username) {
        List<Follow> follow = followRepository.findAllByFollowerUser_Username(username);
        List<UserDto> followings = follow.stream().map(f -> UserDto.builder()
                .id(f.getFollowingUser().getId())
                .username(f.getFollowingUser().getUsername())
                .name(f.getFollowingUser().getName())
                .picture(f.getFollowingUser().getPicture())
                .userJob(f.getFollowingUser().getUsername())
                .userDesc(f.getFollowingUser().getDesc())
                .userTier(f.getFollowingUser().getTier())
                .build())
                .collect(Collectors.toList());

        return followings;
    }

    /**
     * 팔로워 목록 보기
     * @param username
     * @return 나를 팔로잉중인 User dto 리스트
     */
    //TODO 페이징
    public List<UserDto> getFollowerList(String username) {
        List<Follow> follow = followRepository.findAllByFollowingUser_Username(username);
        List<UserDto> followers = follow.stream().map(f -> UserDto.builder()
                .id(f.getFollowerUser().getId())
                .username(f.getFollowerUser().getUsername())
                .name(f.getFollowerUser().getName())
                .picture(f.getFollowerUser().getPicture())
                .userJob(f.getFollowerUser().getUsername())
                .userDesc(f.getFollowerUser().getDesc())
                .userTier(f.getFollowerUser().getTier())
                .build())
                .collect(Collectors.toList());

        return followers;
    }

}
