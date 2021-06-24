package com.githerb.domain.plant.dto.response;

import com.githerb.domain.plant.dto.CommitLanguageDto;
import com.githerb.domain.plant.dto.CommitDto;
import com.githerb.domain.plant.type.PlantStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponsePlantInfo {

    private Long id;
    private Long userId;
    private String frontName;
    private String backName;
    private Integer plantLevel;
    private PlantStatus plantStatus;
    private String repoName;
    private String repoDesc;
    private LocalDateTime startDate;
    private LocalDateTime deadLine;
    private int decimalDay;
    private int commitCycle;
    private int commitCount;
    private int totalCommit;
    private List<CommitDto> commit;
    private List<CommitLanguageDto> comLang;


    @Builder
    public ResponsePlantInfo(Long id, Long userId, String frontName, String backName, String repoName, String repoDesc, Integer plantLevel, PlantStatus plantStatus,
                             LocalDateTime startDate, LocalDateTime deadLine, int decimalDay, int commitCycle, int commitCount, int totalCommit,
                             List<CommitDto> commit, List<CommitLanguageDto> comLang) {
        this.id = id;
        this.userId = userId;
        this.frontName = frontName;
        this.backName = backName;
        this.repoName = repoName;
        this.repoDesc = repoDesc;
        this.plantLevel = plantLevel;
        this.plantStatus = plantStatus;
        this.startDate = startDate;
        this.deadLine = deadLine;
        this.decimalDay = decimalDay;
        this.commitCycle = commitCycle;
        this.commitCount = commitCount;
        this.totalCommit = totalCommit;
        this.commit = commit;
        this.comLang = comLang;
    }
}
