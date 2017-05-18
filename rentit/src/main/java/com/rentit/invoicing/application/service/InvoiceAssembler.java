package com.rentit.invoicing.application.service;

import com.rentit.inventory.application.dto.PlantInventoryEntryDTO;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.rest.controller.InventoryRestController;
import com.rentit.invoicing.InvoiceController;
import com.rentit.invoicing.application.dto.InvoiceDTO;
import com.rentit.invoicing.domain.models.Invoice;
import com.rentit.sales.application.service.PurchaseOrderAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by erdem on 16.05.17.
 */
@Service
public class InvoiceAssembler extends ResourceAssemblerSupport<Invoice, InvoiceDTO> {
    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;
    public InvoiceAssembler() {
        super(InvoiceController.class, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO toResource(Invoice invoice) {
        InvoiceDTO dto = createResourceWithId(invoice.getId(), invoice);
        dto.set_id(invoice.getId());
        dto.setAmount(invoice.getPurchaseOrder().getTotal());
        dto.setDueDate(invoice.getDueDate());
        dto.setPurchaseOrderDTO(purchaseOrderAssembler.toResource(invoice.getPurchaseOrder()));
        return dto;
    }
}
