package com.rentit.common.component;

/**
 * Created by erdem on 16.05.17.
 */

import com.rentit.invoicing.application.service.InvoicingService;
import com.rentit.invoicing.domain.models.InvoiceStatus;
import com.rentit.invoicing.domain.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ScheduledTask {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoicingService invoiceService;

    @Scheduled(cron = "0 0 9 * * MON-FRI")
    public void sendNotificationForInvoices() {
        invoiceRepository.findAllByInvoiceStatus(InvoiceStatus.SENT)
                .stream()
                .filter(invoice -> invoice.getDueDate().isBefore(LocalDate.now()))
                .forEach(invoiceService::sendReminder);
    }

}