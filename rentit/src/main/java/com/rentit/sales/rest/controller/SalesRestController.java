package com.rentit.sales.rest.controller;

import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.application.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * Created by lgarcia on 3/10/2017.
 */
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
        return new ResponseEntity<PurchaseOrderDTO>(poDTO, headers, HttpStatus.CREATED);
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

    @DeleteMapping("/{id}/accept")
    public PurchaseOrderDTO rejectPurchaseOrder(@PathVariable String id) throws Exception {
        return salesService.rejectPurchaseOrder(id);
    }

    @DeleteMapping("/{id}")
    public PurchaseOrderDTO closePurchaseOrder(@PathVariable String id) throws Exception {
        return null;
    }

    @ExceptionHandler(PlantNotFoundException.class)
    @ResponseStatus(HttpStatus.PRECONDITION_FAILED)
    public String bindExceptionHandler(Exception ex) {
        return ex.getMessage();
    }
}
