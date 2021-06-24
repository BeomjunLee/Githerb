package com.githerb.domain.plant.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlantAchieveDto {
    private int all;
    private int inProgress;
    private int done;

    @Builder
    public PlantAchieveDto(int all, int inProgress, int done) {
        this.all = all;
        this.inProgress = inProgress;
        this.done = done;
    }
}
