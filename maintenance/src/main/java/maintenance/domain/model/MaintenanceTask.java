package maintenance.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import maintenance.domain.model.BusinessPeriod;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by erdem on 16.05.17.
 */
@Entity
@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class MaintenanceTask {
    @Id
    String id;
    String itemId;
    BusinessPeriod period;
}
