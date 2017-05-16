package com.rentit.invoicing.domain.models;

import com.rentit.sales.domain.model.PurchaseOrder;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by erdem on 16.05.17.
 */
@Entity
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class Invoice {
    @Id
    String id;

    @OneToOne
    PurchaseOrder purchaseOrder;

    @Enumerated(EnumType.STRING)
    InvoiceStatus invoiceStatus;

    LocalDate dueDate;

    public Invoice setPaid(){
        this.invoiceStatus = InvoiceStatus.PAID;
        return this;
    }

}
