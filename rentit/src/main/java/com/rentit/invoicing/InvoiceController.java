package com.rentit.invoicing;

import com.rentit.invoicing.application.dto.InvoiceDTO;
import com.rentit.invoicing.application.service.InvoicingService;
import com.rentit.invoicing.domain.models.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by erdem on 16.05.17.
 */
@RestController("/api/invoice")
public class InvoiceController {
    @Autowired
    InvoicingService invoicingService;


    @GetMapping("{id}")
    public InvoiceDTO findInvoice(@PathVariable String  id){
        return invoicingService.findInvoice(id);
    }

}
