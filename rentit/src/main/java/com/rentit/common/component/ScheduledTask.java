package com.rentit.common.component;

/**
 * Created by erdem on 16.05.17.
 */

import com.rentit.invoicing.application.service.InvoicingService;
import com.rentit.invoicing.domain.models.InvoiceStatus;
import com.rentit.invoicing.domain.repository.InvoiceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Component
public class ScheduledTask {

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    InvoicingService invoiceService;


    private static final Logger log = LoggerFactory.getLogger(ScheduledTask.class);
    @Scheduled(cron = "0 0 9 * * MON-FRI")
    public void sendNotificationForInvoices() {
        log.info("notification send process starts");
        invoiceRepository.findAllByInvoiceStatus(InvoiceStatus.SENT)
                .parallelStream()
                .filter(invoice -> invoice.getDueDate().isBefore(LocalDate.now()))
                .forEach(invoiceService::sendReminder);
        log.info("notification send process ended");
    }





}