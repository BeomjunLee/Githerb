package com.githerb.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.githerb.domain.plant.dto.PlantAchieveDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private Long id;
    private String username;
    private String name;
    private String picture;
    private String userJob;
    private String userDesc;
    private String userTier;
    private Integer following;
    private Integer follower;
    private PlantAchieveDto plantAchieve;

    @Builder
    public UserDto(Long id, String username, String name, String picture, String userJob, String userDesc,
                   String userTier, Integer following, Integer follower, PlantAchieveDto plantAchieve) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.picture = picture;
        this.userJob = userJob;
        this.userDesc = userDesc;
        this.userTier = userTier;
        this.following = following;
        this.follower = follower;
        this.plantAchieve = plantAchieve;
    }
}
