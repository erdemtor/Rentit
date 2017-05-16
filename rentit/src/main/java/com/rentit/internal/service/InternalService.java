package com.rentit.internal.service;

import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.application.service.InventoryService;
import com.rentit.inventory.application.service.PlantInventoryEntryAssembler;
import com.rentit.inventory.domain.repository.PlantInventoryEntryRepository;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.application.service.PurchaseOrderAssembler;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.domain.model.PurchaseOrder;
import com.rentit.sales.domain.repository.PurchaseOrderRepository;
import com.rentit.sales.infrastructure.SalesIdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */

@Service
public class InternalService {
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;

    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderDTO updateStatus(String id, POStatus status) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if(po == null) throw new PurchaseOrderNotFoundException(id);
        po.updateStatus(status);
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
    public List<PlantInventoryEntryDTO> findToBeDispatchedOn(LocalDate startDate) {
        return plantInventoryEntryAssembler.toResources(purchaseOrderRepository.findToBeDispatchedOn(startDate));
    }

    public List<PurchaseOrderDTO> findAllPurchaseOrders() {
        return purchaseOrderAssembler.toResources(purchaseOrderRepository.findAll());
    }
}
