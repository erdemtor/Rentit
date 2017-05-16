package com.rentit.invoicing.application.dto;

import com.rentit.common.rest.ResourceSupport;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by erdem on 16.05.17.
 */
@Data
public class InvoiceDTO extends ResourceSupport {
    String _id;
    BigDecimal amount;
    LocalDate dueDate;
}
