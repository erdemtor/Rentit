package com.rentit.inventory.domain.repository;

import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.model.PlantInventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantInventoryItemRepository extends JpaRepository<PlantInventoryItem, String>{
    PlantInventoryItem findOneByPlantInfo(PlantInventoryEntry entry);

    List<PlantInventoryItem> findAllByPlantInfo(PlantInventoryEntry item);
}
