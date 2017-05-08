package com.rentit.sales.web.dto;

import com.rentit.common.application.dto.BusinessPeriodDTO;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CatalogQueryDTO {
    String name;
    BusinessPeriodDTO rentalPeriod;
}
