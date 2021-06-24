package com.githerb.domain.plant.type;

import lombok.Getter;

@Getter
public enum PlantStatus {
    DONE("완"), FAILED("실패"), IN_PROGRESS("진행중");

    public String status;

    PlantStatus(String status) {
        this.status = status;
    }
}
