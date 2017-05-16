package com.rentit.inventory.application.dto;

import com.rentit.common.domain.model.BusinessPeriod;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by erdem on 16.05.17.
 */
@Data
public class MaintenanceTaskDTO extends ResourceSupport {
    String _id;
    String itemId;
    BusinessPeriod businessPeriod;
}
