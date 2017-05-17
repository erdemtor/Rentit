package com.rentit.inventory.application.service;

import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.rest.controller.InventoryRestController;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    public PlantInventoryEntryAssembler() {
        super(InventoryRestController.class, PlantInventoryEntryDTO.class);
    }
    @Override
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plantInventoryEntry) {
        PlantInventoryEntryDTO dto = createResourceWithId(plantInventoryEntry.getId(), plantInventoryEntry);
        dto.set_id(plantInventoryEntry.getId());
        dto.setName(plantInventoryEntry.getName());
        dto.setDescription(plantInventoryEntry.getDescription());
        dto.setPrice(plantInventoryEntry.getPrice());
        return dto;
    }

}
