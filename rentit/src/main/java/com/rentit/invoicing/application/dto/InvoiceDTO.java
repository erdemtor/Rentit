package com.rentit.invoicing.application.dto;

import com.rentit.common.rest.ResourceSupport;
import com.rentit.sales.application.dto.PurchaseOrderDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by erdem on 16.05.17.
 */
@Data
public class InvoiceDTO extends ResourceSupport {
    String _id;
    PurchaseOrderDTO purchaseOrderDTO;
    BigDecimal amount;
    LocalDate dueDate;
}
