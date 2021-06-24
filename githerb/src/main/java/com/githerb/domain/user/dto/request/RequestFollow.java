package com.githerb.domain.user.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RequestFollow {
    private Long followUserId;

    @Builder
    public RequestFollow(Long followUserId) {
        this.followUserId = followUserId;
    }
}
