package com.rentit.internal.rest;

import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderRejectionPeriodException;
import com.rentit.internal.service.InternalService;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.rentit.sales.domain.model.POStatus.DISPATCHED;

/**
 * Created by erdem on 16.05.17.
 */

@Controller
@PreAuthorize("hasRole('ROLE_employee')")
@RequestMapping("/api/internal/orders/")
public class InternalRestController {
    @Autowired
    InternalService internalService;
    @PostMapping("/{id}/dispatched")
    public PurchaseOrderDTO setDispatched(@PathVariable String id) throws PurchaseOrderNotFoundException {
        return internalService.updateStatus(id, DISPATCHED);
    }

    @PostMapping("/{purchaseOrderId}/returned")
    public PurchaseOrderDTO updateStatus(@PathVariable String purchaseOrderId) throws PurchaseOrderNotFoundException {
        return internalService.handleReturned(purchaseOrderId);
    }

    @GetMapping("/on")
    public List<PlantInventoryEntryDTO> getPurchaseOrderByStartDate(@RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate) throws Exception {
        return internalService.findToBeDispatchedOn(startDate.orElse(LocalDate.now()));
    }

    @GetMapping
    public List<PurchaseOrderDTO> getAllPurchaseOrders() throws Exception {
        return internalService.findAllPurchaseOrders();
    }

    @ExceptionHandler({
            PlantNotFoundException.class,
            PurchaseOrderRejectionPeriodException.class,
            PurchaseOrderNotFoundException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String bindExceptionHandler(Exception ex) {
        Map<String, String> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return String.valueOf(new JSONObject(map));
    }

}
