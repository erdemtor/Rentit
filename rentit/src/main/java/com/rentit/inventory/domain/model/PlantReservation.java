package com.rentit.inventory.domain.model;

import com.rentit.common.domain.model.BusinessPeriod;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
@AllArgsConstructor(staticName = "of")
public class PlantReservation {
    @Id
    String id;

    @ManyToOne
    PlantInventoryItem plant;

    @Embedded
    BusinessPeriod schedule;
}
