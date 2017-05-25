package com.rentit.internal.rest;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.internal.service.InternalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import retrofit.http.GET;

import java.time.LocalDate;

/**
 * Created by erdem on 22.05.17.
 */
@RestController
@CrossOrigin
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

    @PostMapping()
    public String createMaintenanceTask(@RequestBody String body) throws UnirestException {
        return Unirest.post("http://0.0.0.0:8888/api/maintenance").body(body).asString().getBody();
    }

    @GetMapping
    public String getTasks() throws UnirestException {
        return Unirest.get("http://0.0.0.0:8888/api/maintenance").asString().getBody();
    }

}
