package com.rentit.inventory.rest.controller;

import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.application.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory/plants")
public class InventoryRestController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
            if (endDate.isBefore(startDate))
                throw new IllegalArgumentException("Something wrong with the requested period ('endDate' happens before 'startDate')");
            return inventoryService.findAvailablePlants(plantName.orElse(""), startDate, endDate);

    }

    @GetMapping("/{id}")
    public PlantInventoryEntryDTO show(@PathVariable String id) throws PlantNotFoundException {
        return inventoryService.findPlant(id);
    }

    @GetMapping("/{id}/availability")
    public boolean checkAvailability(@PathVariable String id,
                                     @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate startDate,
                                     @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate endDate) throws PlantNotFoundException {
        return inventoryService.isAvailable(id, startDate, endDate);
    }




    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleException(Exception exc) {
        return exc.getMessage();
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(Exception exc) {
        return exc.getMessage();
    }
}

