package com.rentit.inventory.infrastructure;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class InventoryIdentifierFactory {
    public String nextPlantInventoryEntryID() {
        return UUID.randomUUID().toString().substring(0,5);
    }
}
