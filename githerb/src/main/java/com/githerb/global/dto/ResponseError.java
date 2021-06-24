package com.githerb.global.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseError {
    private int status;
    private String message;

    @Builder
    public ResponseError(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
