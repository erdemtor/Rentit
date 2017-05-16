package maintenance.domain.dto;

import lombok.Data;
import maintenance.domain.model.BusinessPeriod;
import org.springframework.hateoas.ResourceSupport;

/**
 * Created by erdem on 16.05.17.
 */
@Data
public class MaintenanceTaskDTO extends ResourceSupport {
    String _id;
    String itemId;
    BusinessPeriod businessPeriod;
}
