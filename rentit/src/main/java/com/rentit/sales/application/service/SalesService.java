package com.rentit.sales.application.service;

import com.rentit.common.application.exceptions.PlantInventoryEntryNotAvailableException;
import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderRejectionPeriodException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.application.service.InventoryService;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.model.PlantReservation;
import com.rentit.inventory.domain.repository.PlantInventoryEntryRepository;
import com.rentit.inventory.domain.repository.PlantReservationRepository;
import com.rentit.invoicing.domain.models.Customer;
import com.rentit.invoicing.domain.repository.CustomerRepository;
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
    @Autowired
    CustomerRepository customerRepo;
    @Autowired
    PlantReservationRepository plantReservationRepository;


    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO purchaseOrderDTO, String customerEmail) throws PlantNotFoundException {
        Customer customer = customerRepo.findByEmail(customerEmail);
        PlantInventoryEntry plantInventoryEntry = plantInventoryEntryRepository.findOne(purchaseOrderDTO.getPlant().get_id());
        if(plantInventoryEntry == null) throw new PlantNotFoundException(purchaseOrderDTO.getPlant().get_id());
        BusinessPeriod rentalPeriod = BusinessPeriod.of(purchaseOrderDTO.getRentalPeriod().getStartDate(), purchaseOrderDTO.getRentalPeriod().getEndDate());
        PurchaseOrder po = PurchaseOrder.of(identifierFactory.nextPurchaseOrderID(), plantInventoryEntry, rentalPeriod, customer);
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

    public PurchaseOrder assignNewReservationForPurchaseOrder(PurchaseOrder po) throws PlantNotFoundException {
        try {
            PlantReservation plantReservation = inventoryService.createPlantReservation(po.getPlant(), po.getRentalPeriod());
            po.confirmReservation(plantReservation);
            return  purchaseOrderRepository.save(po);
        } catch (PlantNotFoundException e) {
            po.updateStatus(POStatus.REJECTED);
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


    public PurchaseOrderDTO updateRentalPeriod(String purchaseOrderId, LocalDate newEndDate) throws PurchaseOrderNotFoundException, PlantInventoryEntryNotAvailableException, PlantNotFoundException {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderId);
        if (purchaseOrder == null) {
            throw new PurchaseOrderNotFoundException(purchaseOrderId);
        }
        if (!inventoryService.isAvailable(purchaseOrder.getPlant().getId(), purchaseOrder.getRentalPeriod().getEndDate(), newEndDate)) {
            throw new PlantInventoryEntryNotAvailableException(purchaseOrder.getPlant().getName(),purchaseOrder.getRentalPeriod().getStartDate(),newEndDate);
        }

        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(purchaseOrder.updateRentalPeriod(purchaseOrder.getRentalPeriod().getStartDate(), newEndDate)));
    }


}
