package com.githerb.domain.plant.repository;

import com.githerb.domain.plant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlantRepository extends JpaRepository<Plant, Long> {
}
