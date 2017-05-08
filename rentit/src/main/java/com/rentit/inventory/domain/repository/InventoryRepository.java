package com.rentit.inventory.domain.repository;

import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by lgarcia on 2/17/2017.
 */
@Repository
public interface InventoryRepository extends JpaRepository<PlantInventoryEntry, String>, CustomInventoryRepository {
    @Query("select i from PlantInventoryItem i where i.plantInfo = ?1 and i.equipmentCondition = 'SERVICEABLE' and i not in (select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)")
    List<PlantInventoryItem> findAvailableInventoryItems(PlantInventoryEntry plantInventoryEntry, LocalDate startDate, LocalDate endDate);
}
