package com.rentit.invoicing.domain.repository;

import com.rentit.invoicing.domain.models.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by erdem on 16.05.17.
 */
public interface InvoiceRepository extends JpaRepository<Invoice, String> {}
