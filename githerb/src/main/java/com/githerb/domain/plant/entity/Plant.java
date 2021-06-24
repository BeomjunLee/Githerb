package com.githerb.domain.plant.entity;

import com.githerb.domain.plant.dto.request.RequestEnrollPlant;
import com.githerb.domain.user.entity.User;
import com.githerb.domain.plant.type.FrontNameType;
import com.githerb.domain.plant.type.PlantStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Plant {

    @Id
    @GeneratedValue
    @Column(name = "plant_id")
    private Long id;
    private String frontName;
    private String backName;
    private String repoName;
    private String desc;
    @CreatedDate
    private LocalDateTime startDate;
    private LocalDateTime deadLine;
    private int commitCycle;
    @Enumerated
    private PlantStatus plantStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Plant(String backName, String repoName, String desc, int commitCycle, PlantStatus plantStatus) {
        this.backName = backName;
        this.repoName = repoName;
        this.desc = desc;
        this.commitCycle = commitCycle;
        this.plantStatus = plantStatus;
    }

    public static Plant createPlant(RequestEnrollPlant requestEnrollPlant, User user) {
        Plant plant = Plant.builder()
                .backName(requestEnrollPlant.getPlantName())
                .repoName(requestEnrollPlant.getRepoName())
                .desc(requestEnrollPlant.getRepoDesc())
                .commitCycle(requestEnrollPlant.getCommitCycle())
                .plantStatus(PlantStatus.IN_PROGRESS)
                .build();
        plant.randomFrontNameType();
        plant.stringToLocalDateTime(requestEnrollPlant.getDeadLine());
        plant.setUser(user);

        return plant;
    }

    private void randomFrontNameType() {
        int random = new Random().nextInt(FrontNameType.values().length);
        this.frontName = FrontNameType.values()[random].getType();
    }

    private void stringToLocalDateTime(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.deadLine = LocalDateTime.parse(date, dateTimeFormatter);
    }

    public void setUser(User user) {
        this.user = user;
        user.getPlants().add(this);
    }
}