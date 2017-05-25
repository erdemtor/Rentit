package com.rentit.internal.service;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rentit.common.application.exceptions.PlantNotFoundException;
import com.rentit.common.application.exceptions.PurchaseOrderNotFoundException;
import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.application.service.PlantInventoryEntryAssembler;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.repository.PlantInventoryItemRepository;
import com.rentit.invoicing.application.service.InvoicingService;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.application.service.PurchaseOrderAssembler;
import com.rentit.sales.application.service.SalesService;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.domain.model.PurchaseOrder;
import com.rentit.sales.domain.repository.PurchaseOrderRepository;
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
    InvoicingService invoicingService;
    @Autowired
    PlantInventoryItemRepository plantInventoryItemRepository;
    @Autowired
    SalesService salesService;
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderDTO updateStatus(String id, POStatus status) throws PurchaseOrderNotFoundException {
        PurchaseOrder po = purchaseOrderRepository.findOne(id);
        if(po == null) throw new PurchaseOrderNotFoundException(id);
        po.updateStatus(status);
        return purchaseOrderAssembler.toResource(purchaseOrderRepository.save(po));
    }
    public List<PurchaseOrderDTO> findToBeDispatchedOn(LocalDate startDate) {
        return purchaseOrderAssembler.toResources(purchaseOrderRepository.findToBeDispatchedOn(startDate));
    }

    public List<PurchaseOrderDTO> findAllPurchaseOrders() {
        return purchaseOrderAssembler.toResources(purchaseOrderRepository.findAll());
    }

    public PurchaseOrderDTO handleReturned(String purchaseOrderId) throws PurchaseOrderNotFoundException {
        invoicingService.sendInvoice(purchaseOrderId);
        return this.updateStatus(purchaseOrderId, POStatus.PLANT_RETURNED);

    }

    public String updatePurchaseOrdersAboutMaintenance(String plantId, BusinessPeriod task) {
        PlantInventoryEntry entry = plantInventoryItemRepository.findOne(plantId).getPlantInfo();
        List<PurchaseOrder> purchaseOrders = purchaseOrderRepository.findAllByPlant(entry.getId());
        purchaseOrders
                .stream()
                .filter(order ->
                        order.getPlantReservation().getPlant().getId().equals(plantId) &&
                        task.isIntersectingWith(order.getRentalPeriod()))
                .forEach(purchaseOrder -> {
                    try {
                        salesService.assignNewReservationForPurchaseOrder(purchaseOrder);
                    } catch (PlantNotFoundException e) {
                        notifyCustomerAboutCancel(purchaseOrder);
                    }
                });
        return "success";
    }

    public String notifyCustomerAboutCancel(PurchaseOrder purchaseOrder) {
        try {
            String url = purchaseOrder.getCustomer().getBase_url()+"/api/procurement/phr/order/"+purchaseOrder.getId()+"/cancel";
            System.out.println(url);
            System.out.println(Unirest.delete(url).basicAuth("site", "site").asString().getBody());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return null;
    }
}
