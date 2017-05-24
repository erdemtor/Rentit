package com.rentit.internal.rest;

import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.internal.service.InternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * Created by erdem on 22.05.17.
 */
@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceCommunicationRestController {
    @Autowired
    InternalService internalService;

    @PutMapping("/{plantId}")
    public String getAllPurchaseOrders(@PathVariable String plantId,
                                       @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                       @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws Exception {
        return internalService.updatePurchaseOrdersAboutMaintenance(plantId, BusinessPeriod.of(startDate, endDate));
    }

}
