package com.rentit.inventory.application.dto;

import com.rentit.common.rest.ResourceSupport;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String _id;
    String name;
    String description;
    BigDecimal price;
}
