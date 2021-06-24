package com.githerb.global.type;

import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum ResultType {
    RESULT_ENROLL_PLANT(CREATED.value(), "식물 등록에 성공하였습니다"),
    RESULT_FOLLOW(OK.value(), "팔로우 되었습니다"),
    RESULT_UN_FOLLOW(OK.value(), "언팔로우 되었습니다");

    private int status;
    private String message;

    ResultType(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
