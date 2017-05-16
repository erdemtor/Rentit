package com.rentit.invoicing.domain.models;

import com.rentit.sales.domain.model.PurchaseOrder;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by erdem on 16.05.17.
 */
@Entity
@Data
public class Invoice {
    @Id
    String id;

    @OneToOne
    PurchaseOrder purchaseOrder;

    @Enumerated(EnumType.STRING)
    InvoiceStatus invoiceStatus;

    public Invoice setPaid(){
        this.invoiceStatus = InvoiceStatus.PAID;
        return this;
    }

    @ManyToOne
    Customer customer;

}
