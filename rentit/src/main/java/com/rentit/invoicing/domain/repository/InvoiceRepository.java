package com.rentit.invoicing.domain.repository;

import com.rentit.sales.domain.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by erdem on 16.05.17.
 */
public interface InvoiceRepository extends JpaRepository<PurchaseOrder, String> {}
