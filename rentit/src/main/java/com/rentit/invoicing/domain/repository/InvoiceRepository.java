package com.rentit.invoicing.domain.repository;

import com.rentit.invoicing.domain.models.Invoice;
import com.rentit.invoicing.domain.models.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    List<Invoice> findAllByInvoiceStatus(InvoiceStatus status);

}
