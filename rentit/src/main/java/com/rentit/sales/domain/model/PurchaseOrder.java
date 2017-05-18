package com.rentit.sales.domain.model;

import com.rentit.common.domain.model.BusinessPeriod;
import com.rentit.inventory.domain.model.PlantInventoryEntry;
import com.rentit.inventory.domain.model.PlantReservation;
import com.rentit.invoicing.domain.models.Customer;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class PurchaseOrder {
    @Id
    String id;

    @ManyToOne
    PlantInventoryEntry plant;
    @Embedded
    BusinessPeriod rentalPeriod;

    @ManyToOne
    Customer customer;

    @OneToOne
    PlantReservation plantReservation;

    @Enumerated(EnumType.STRING)
    POStatus status;

    @Column(precision = 8, scale = 2)
    BigDecimal total;

    public static PurchaseOrder of(String id, PlantInventoryEntry plant, BusinessPeriod rentalPeriod, Customer customer) {
        PurchaseOrder po = new PurchaseOrder();
        po.id = id;
        po.plant = plant;
        po.rentalPeriod = rentalPeriod;
        po.status = POStatus.CREATED;
        po.customer = customer;
        return po;
    }

    public void confirmReservation(PlantReservation plantReservation, BigDecimal price) {
        this.plantReservation = plantReservation;
        long workingDays = rentalPeriod.numberOfWorkingDays();
        total = price.multiply(BigDecimal.valueOf(workingDays));
        status = POStatus.ACCEPTED;
    }
    public void confirmReservation(PlantReservation plantReservation) {
      this.confirmReservation(plantReservation, this.plant.getPrice());
    }

    public void handleRejection() {
        status = POStatus.REJECTED;
    }
    public void cancel() {
        status = POStatus.REJECTED_BY_CUSTOMER;
    }


    public void handleClose() {
        status = POStatus.CLOSED;
    }

    public void handleAcceptance() {
        status = POStatus.ACCEPTED;
    }

    public PurchaseOrder updateStatus(POStatus status) {
        this.status = status;
        return this;
    }

    public PurchaseOrder updateRentalPeriod(LocalDate startDate, LocalDate endDate) {
        this.getPlantReservation().setSchedule(BusinessPeriod.of(startDate, endDate));
        this.rentalPeriod = BusinessPeriod.of(startDate, endDate);
        this.confirmReservation(this.getPlantReservation());
        return this;
    }
}
