package com.rentit.invoicing.service;

import com.rentit.invoicing.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by erdem on 16.05.17.
 */
@Service
public class InvoicingService {

    @Autowired
    InvoiceRepository invoiceRepository;

    public void sendInvoice(String purchaseOrderId) {


    }
}
