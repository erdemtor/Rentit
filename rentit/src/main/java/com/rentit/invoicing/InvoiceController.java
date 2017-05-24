package com.rentit.invoicing;

import com.rentit.invoicing.application.dto.InvoiceDTO;
import com.rentit.invoicing.application.service.InvoicingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by erdem on 16.05.17.
 */
@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {
    @Autowired
    InvoicingService invoicingService;


    @GetMapping("/{id}")
    public InvoiceDTO findInvoice(@PathVariable String  id){
        System.out.println(id);
        return invoicingService.findInvoice(id);
    }


    @PostMapping("/{id}/remittance")
    public InvoiceDTO receiveRemittance(@PathVariable String invoiceId){
        return invoicingService.payInvoice(invoiceId);
    }

}
