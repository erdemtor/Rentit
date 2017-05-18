package com.rentit.inventory.application.service;

import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.rest.ExtendedLink;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.rest.controller.InventoryRestController;
import com.rentit.sales.rest.controller.SalesRestController;
import org.eclipse.jetty.http.HttpMethod;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;
import retrofit.http.GET;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Service
public class PlantInventoryEntryAssembler extends ResourceAssemblerSupport<PlantInventoryEntry, PlantInventoryEntryDTO> {
    public PlantInventoryEntryAssembler() {
        super(InventoryRestController.class, PlantInventoryEntryDTO.class);
    }
    @Override
    public PlantInventoryEntryDTO toResource(PlantInventoryEntry plantInventoryEntry) {
        PlantInventoryEntryDTO dto = createResourceWithId(plantInventoryEntry.getId(), plantInventoryEntry);
        try {
            dto.add(new ExtendedLink(
                    ControllerLinkBuilder.linkTo(methodOn(InventoryRestController.class)
                            .show(plantInventoryEntry.getId())).toString(), "self", org.springframework.http.HttpMethod.GET));
        } catch (PlantNotFoundException e) {
            e.printStackTrace();
        }
        dto.set_id(plantInventoryEntry.getId());
        dto.setName(plantInventoryEntry.getName());
        dto.setDescription(plantInventoryEntry.getDescription());
        dto.setPrice(plantInventoryEntry.getPrice());
        return dto;
    }

}
