package com.githerb.domain.user.controller;
import com.githerb.domain.user.dto.UserDto;
import com.githerb.domain.user.dto.request.RequestFollow;
import com.githerb.global.dto.ResponseResult;
import com.githerb.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/followings")
    public ResponseResult following(@AuthenticationPrincipal OAuth2User oAuth2User,
                                    @RequestBody RequestFollow requestFollow) {
        return userService.addFollowing(oAuth2User.getName() , requestFollow.getFollowUserId());
    }

    @GetMapping("/followings")
    public List<UserDto> getFollowings(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return userService.getFollowingList(oAuth2User.getName());
    }

    @GetMapping("/followers")
    public List<UserDto> getFollowers(@AuthenticationPrincipal OAuth2User oAuth2User) {
        return userService.getFollowerList(oAuth2User.getName());
    }
}
