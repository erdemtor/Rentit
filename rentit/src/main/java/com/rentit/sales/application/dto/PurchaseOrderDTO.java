package com.rentit.sales.application.dto;

import com.rentit.common.application.dto.BusinessPeriodDTO;
import com.rentit.common.rest.ResourceSupport;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.sales.domain.model.POStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseOrderDTO extends ResourceSupport {
    String _id;
    PlantInventoryEntryDTO plant;
    BusinessPeriodDTO rentalPeriod;
    BigDecimal total;
    POStatus status;
}
