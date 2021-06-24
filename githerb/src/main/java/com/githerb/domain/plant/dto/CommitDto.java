package com.githerb.domain.plant.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitDto {
    private LocalDateTime date;
    private String committer;
    private String message;

    @Builder
    public CommitDto(LocalDateTime date, String committer, String message) {
        this.date = date;
        this.committer = committer;
        this.message = message;
    }

    public void setCommitter(String committer) {
        this.committer = committer;
    }
}
