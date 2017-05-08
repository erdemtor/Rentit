package com.rentit.inventory.domain.repository;

import com.rentit.inventory.domain.model.PlantInventoryEntry;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by lgarcia on 2/17/2017.
 */
@SuppressWarnings("ALL")
public class InventoryRepositoryImpl implements CustomInventoryRepository {
    @Autowired
    EntityManager em;

    @Override
    public List<PlantInventoryEntry> findAvailable(String name, LocalDate startDate, LocalDate endDate) {
        TypedQuery<PlantInventoryEntry> query = em.createQuery(
                "select i.plantInfo from PlantInventoryItem i where lower(i.plantInfo.name) like ?1 and i.equipmentCondition = com.rentit.inventory.domain.model.EquipmentCondition.SERVICEABLE and i not in (select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)"
                , PlantInventoryEntry.class)
                .setParameter(1, "%" + name.toLowerCase() + "%")
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        return query.getResultList();
    }

    @Override
    public boolean isAvailable(String id, LocalDate startDate, LocalDate endDate) {
        TypedQuery<PlantInventoryEntry> query = em.createQuery(
                "select 1 from PlantInventoryItem i where lower(i.plantInfo.id) = ?1 and i.equipmentCondition = com.rentit.inventory.domain.model.EquipmentCondition.SERVICEABLE and i not in (select r.plant from PlantReservation r where ?2 < r.schedule.endDate and ?3 > r.schedule.startDate)"
                , PlantInventoryEntry.class)
                .setParameter(1,  id)
                .setParameter(2, startDate)
                .setParameter(3, endDate);
        return !query.getResultList().isEmpty();
    }

}
