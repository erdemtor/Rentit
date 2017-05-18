package com.rentit.inventory.application.dto;

import com.rentit.common.application.dto.BusinessPeriodDTO;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.common.rest.ResourceSupport;
import lombok.Data;


/**
 * Created by erdem on 16.05.17.
 */
@Data
public class MaintenanceTaskDTO extends ResourceSupport {
    String _id;
    String itemId;
    BusinessPeriodDTO businessPeriod;
}
