package com.githerb.domain.plant.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommitLanguageDto implements Serializable {
    private String language;
    private double percentage;
    private Long usedLine;

    @Builder
    public CommitLanguageDto(String language, double percentage, Long usedLine) {
        this.language = language;
        this.percentage = percentage;
        this.usedLine = usedLine;
    }

    public void calculatePercentage(double total) {
        this.percentage = Math.round(usedLine.doubleValue() / total * 1000) / 10.0;
    }
}
