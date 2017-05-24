package com.rentit.invoicing.application.service;

import com.mashape.unirest.http.Unirest;
import com.rentit.inventory.infrastructure.InventoryIdentifierFactory;
import com.rentit.invoicing.application.dto.InvoiceDTO;
import com.rentit.invoicing.domain.models.Invoice;
import com.rentit.invoicing.domain.repository.InvoiceRepository;
import com.rentit.sales.domain.model.POStatus;
import com.rentit.sales.domain.model.PurchaseOrder;
import com.rentit.sales.domain.repository.PurchaseOrderRepository;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

import static com.rentit.invoicing.domain.models.InvoiceStatus.SENT;
import static com.rentit.sales.domain.model.POStatus.CLOSED;

/**
 * Created by erdem on 16.05.17.
 */
@Service
public class InvoicingService {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;
    @Autowired
    InvoiceAssembler invoiceAssembler;
    @Autowired
    private RestTemplate restTemplate;
    public void sendInvoice(String purchaseOrderId) {
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findOne(purchaseOrderId);
        Invoice invoice = Invoice.of(new InventoryIdentifierFactory().nextPlantInventoryEntryID(),
                purchaseOrder,
                SENT,
                LocalDate.now().plusDays(14));
        invoiceRepository.save(invoice);
        InvoiceDTO invoiceDTO = invoiceAssembler.toResource(invoice);
        HttpHeaders headers;
        String plainCreds = "site" + ":" + "site";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);
        headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);
        HttpEntity<?> request = new HttpEntity<>(invoiceDTO, headers);
        try {
            restTemplate.exchange(
                    purchaseOrder.getCustomer().getBase_url()+ "/api/invoicing/invoice",
                    HttpMethod.POST,
                    request,
                    InvoiceDTO.class
            );
        }catch (Exception e){
            e.printStackTrace();
        }
        purchaseOrder.updateStatus(POStatus.INVOICED);

    }

    public InvoiceDTO findInvoice(String id) {
        return invoiceAssembler.toResource(invoiceRepository.findOne(id));
    }

    public void sendReminder(Invoice invoice) {
        Unirest
        .post(invoice.getPurchaseOrder().getCustomer().getBase_url()+ "/api/invoicing/invoice")
                .body(invoiceAssembler.toResource(invoice));
    }

    public InvoiceDTO payInvoice(String invoiceId) {
        purchaseOrderRepository.save(invoiceRepository.findOne(invoiceId).getPurchaseOrder().updateStatus(CLOSED));
        return invoiceAssembler.toResource(invoiceRepository.save(invoiceRepository.findOne(invoiceId).setPaid()));
    }
}
