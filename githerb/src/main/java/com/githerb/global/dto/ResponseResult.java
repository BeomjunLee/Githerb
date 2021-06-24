package com.githerb.global.dto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseResult {

    private int status;
    private String message;

    @Builder
    public ResponseResult(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
