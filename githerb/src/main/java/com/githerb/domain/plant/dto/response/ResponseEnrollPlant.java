package com.githerb.domain.plant.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.githerb.domain.plant.dto.PlantDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEnrollPlant {
    private String message;
    private PlantDto data;

    @Builder
    public ResponseEnrollPlant(String message, PlantDto data) {
        this.message = message;
        this.data = data;
    }
}
