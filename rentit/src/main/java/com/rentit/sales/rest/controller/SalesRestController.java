package com.rentit.sales.rest.controller;

import com.rentit.common.application.exceptions.PlantInventoryEntryNotAvailableException;
import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderRejectionPeriodException;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.application.service.SalesService;
import com.rentit.sales.domain.model.POStatus;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sales/orders")
public class SalesRestController {
    @Autowired
    SalesService salesService;

    @PostMapping
    public ResponseEntity<PurchaseOrderDTO> createPurchaseOrder(@RequestBody PurchaseOrderDTO poDTO) throws Exception {
        poDTO = salesService.createPurchaseOrder(poDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(poDTO.getId().getHref()));
        return new ResponseEntity<>(poDTO, headers, HttpStatus.CREATED);
    }

    @PostMapping("/{id}")
    public PurchaseOrderDTO modifyPurchaseOrder(@PathVariable String id,
                                                @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws Exception {
        return salesService.updateRentalPeriod(id, startDate, endDate);
    }

    @GetMapping("/on")
    public List<PlantInventoryEntryDTO> getPurchaseOrderByStartDate(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate) throws Exception {
        return salesService.findToBeDispatchedOn(startDate.orElse(LocalDate.now()));
    }


    @GetMapping
    public List<PurchaseOrderDTO> getAllPurchaseOrders() throws Exception {
        return salesService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrderDTO showPurchaseOrder(@PathVariable String id) throws Exception {
        PurchaseOrderDTO poDTO = salesService.findPurchaseOrder(id);
        return poDTO;
    }

    @PostMapping("/{id}/accept")
    public PurchaseOrderDTO acceptPurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.acceptPurchaseOrder(id);
    }

    @DeleteMapping("/{id}/reject")
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.rejectPurchaseOrder(id);
    }

    @DeleteMapping("/{id}")
    public PurchaseOrderDTO closePurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.closePurchaseOrder(id);
    }

    @ExceptionHandler({ PlantNotFoundException.class,
                        PurchaseOrderRejectionPeriodException.class,
                        PlantInventoryEntryNotAvailableException.class,
                        PurchaseOrderNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String bindExceptionHandler(Exception ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return String.valueOf(new JSONObject(map));
    }

    @PostMapping("/{id}/updatestatus")
    public PurchaseOrderDTO updateStatus(@PathVariable String id, @RequestParam POStatus status) throws PurchaseOrderNotFoundException {
        return salesService.updateStatus(id, status);
    }


}
