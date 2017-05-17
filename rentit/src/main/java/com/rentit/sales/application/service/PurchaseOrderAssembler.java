package com.rentit.sales.application.service;

import com.rentit.common.application.dto.BusinessPeriodDTO;
import com.rentit.common.rest.ExtendedLink;
import com.rentit.inventory.application.service.PlantInventoryEntryAssembler;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.domain.model.PurchaseOrder;
import com.rentit.sales.rest.controller.SalesRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;

@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {
    @Autowired
    PlantInventoryEntryAssembler plantInventoryEntryAssembler;

    public PurchaseOrderAssembler() {
        super(SalesRestController.class, PurchaseOrderDTO.class);
    }

    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = createResourceWithId(purchaseOrder.getId(), purchaseOrder);
        dto.set_id(purchaseOrder.getId());
        dto.setPlant(plantInventoryEntryAssembler.toResource(purchaseOrder.getPlant()));
        dto.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(),purchaseOrder.getRentalPeriod().getEndDate()));
        dto.setTotal(purchaseOrder.getTotal());
        dto.setStatus(purchaseOrder.getStatus());
        try {
            switch (dto.getStatus()) {
                case PENDING:
                    dto.add(new ExtendedLink(
                            ControllerLinkBuilder.linkTo(methodOn(SalesRestController.class)
                                    .acceptPurchaseOrder(dto.get_id())).toString(),
                            "accept", POST));
                    dto.add(new ExtendedLink(
                            ControllerLinkBuilder.linkTo(methodOn(SalesRestController.class)
                                    .rejectPurchaseOrder(dto.get_id())).toString(),
                            "reject", DELETE));
                    break;
                case ACCEPTED:
                    dto.add(new ExtendedLink(
                            ControllerLinkBuilder.linkTo(methodOn(SalesRestController.class)
                                    .closePurchaseOrder(dto.get_id())).toString(),
                            "close", DELETE));
                    dto.add(new ExtendedLink(
                            ControllerLinkBuilder.linkTo(methodOn(SalesRestController.class)
                                    .modifyPurchaseOrder(dto.get_id(),null)).toString(),
                            "extend", POST));
                default:
                    break;
            }
        } catch (Exception e) {e.printStackTrace();}
        return dto;
    }
}
