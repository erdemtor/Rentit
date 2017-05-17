package com.rentit.inventory.domain.repository;

import com.rentit.inventory.domain.model.PlantReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface PlantReservationRepository extends JpaRepository<PlantReservation, String>{
}
