package com.rentit.sales.application.service;

import com.rentit.common.application.exceptions.PlantInventoryEntryNotAvailableException;
import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderRejectionPeriodException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.application.service.InventoryService;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.model.PlantInventoryItem;
import com.rentit.inventory.domain.model.PlantReservation;
import com.rentit.inventory.domain.repository.PlantInventoryEntryRepository;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.domain.model.PurchaseOrder;
import com.rentit.sales.domain.repository.PurchaseOrderRepository;
import com.rentit.sales.infrastructure.SalesIdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.rentit.sales.domain.model.POStatus.DISPATCHED;

@Service
public class SalesService {
    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    PlantInventoryEntryRepository plantInventoryEntryRepository;
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    @Autowired
    InventoryService inventoryService;
    @Autowired
    SalesIdentifierFactory identifierFactory;


    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO) throws PlantNotFoundException {
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(purchaseOrderDTO.getPlant().get_id());
        if(plantInventoryEntry == null) throw new PlantNotFoundException("No such plantInventoryEntry with the id: "+ purchaseOrderDTO.getPlant().get_id());
        BusinessPeriod rentalPeriod = BusinessPeriod.of(purchaseOrderDTO.getRentalPeriod().getStartDate(), purchaseOrderDTO.getRentalPeriod().getEndDate());

        PurchaseOrder po = PurchaseOrder.of(identifierFactory.nextPurchaseOrderID(),
                                            plantInventoryEntry,
                                            rentalPeriod);

        po = purchaseOrderRepository.save(po);
        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(plantInventoryEntry, rentalPeriod);
            po.confirmReservation(plantReservation, plantInventoryEntry.getPrice());
            po = purchaseOrderRepository.save(po);
            return purchaseOrderAssembler.toResource(po);
        } catch (PlantNotFoundException e) {
            po.handleRejection();
            purchaseOrderRepository.save(po);
            throw e;
        }
    }

    public PurchaseOrderDTO findPurchaseOrder(String id) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if(po == null) throw new PurchaseOrderNotFoundException(id);
        return purchaseOrderAssembler.toResource(po);
    }

    public List<PurchaseOrderDTO> findAll() {
        return purchaseOrderAssembler.toResources(purchaseOrderRepository.findAll());
    }

    public PurchaseOrderDTO acceptPurchaseOrder(String id) {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        po.handleAcceptance();
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
    public PurchaseOrderDTO rejectPurchaseOrder(String id) throws PurchaseOrderRejectionPeriodException {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if(po.getStatus().equals(DISPATCHED)) {
            throw new PurchaseOrderRejectionPeriodException("The plant is dispatched, you cannot cancel anymore!");
        }
        po.cancel();
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }

    public PurchaseOrderDTO closePurchaseOrder(String id) {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        po.handleClose();
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));

    }

    public PurchaseOrder updateStatus(String id, POStatus status) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if(po == null) throw new PurchaseOrderNotFoundException(id);
        po.updateStatus(status);
        return purchaseOrderRepository.save(po);
    }

    public PurchaseOrderDTO updateRentalPeriod(String purchaseOrderId, LocalDate startDate, LocalDate endDate) throws PurchaseOrderNotFoundException, PlantInventoryEntryNotAvailableException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderId);
        if (purchaseOrder == null) throw new PurchaseOrderNotFoundException(purchaseOrderId);
        if (!inventoryService.isAvailable(purchaseOrder.getPlant().getId(), startDate, endDate)) throw new PlantInventoryEntryNotAvailableException(purchaseOrder.getPlant().getName(),startDate,endDate);
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(purchaseOrder.updateRentalPeriod(startDate, endDate)));
    }

    public List<PlantInventoryEntry> findToBeDispatchedOn(LocalDate startDate) {
            return purchaseOrderRepository.findToBeDispatchedOn(startDate);
    }
}
