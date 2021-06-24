package com.githerb.domain.plant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.githerb.domain.plant.type.PlantStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlantDto {
    private Long id;
    private String frontName;
    private String backName;
    private PlantStatus plantStatus;
    private String repoName;
    private String repoDesc;
    private LocalDateTime startDate;
    private LocalDateTime deadLine;
    private Integer decimalDay;
    private int commitCycle;
    private Integer commitCount;
    private Integer totalCommit;
    private List<CommitDto> commit;

    @Builder
    public PlantDto(Long id, String frontName, String backName, PlantStatus plantStatus, String repoName, String repoDesc, LocalDateTime startDate,
                    LocalDateTime deadLine, Integer decimalDay, int commitCycle, Integer commitCount, Integer totalCommit, List<CommitDto> commit) {
        this.id = id;
        this.frontName = frontName;
        this.backName = backName;
        this.plantStatus = plantStatus;
        this.repoName = repoName;
        this.repoDesc = repoDesc;
        this.startDate = startDate;
        this.deadLine = deadLine;
        this.decimalDay = decimalDay;
        this.commitCycle = commitCycle;
        this.commitCount = commitCount;
        this.totalCommit = totalCommit;
        this.commit = commit;
    }
}
