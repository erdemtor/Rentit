package maintenance.service;

import maintenance.controller.MaintenanceController;
import maintenance.domain.dto.MaintenanceTaskDTO;
import maintenance.domain.model.MaintenanceTask;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by erdem on 16.05.17.
 */
@Service
public class MaintenanceTaskAssembler extends ResourceAssemblerSupport<MaintenanceTask, MaintenanceTaskDTO> {
    public MaintenanceTaskAssembler(){
            super(MaintenanceController.class, MaintenanceTaskDTO.class);
    }

    @Override
    public MaintenanceTaskDTO toResource(MaintenanceTask maintenanceTask) {
        MaintenanceTaskDTO dto = createResourceWithId(maintenanceTask.getId(), maintenanceTask);
        maintenanceTask.setItemId(maintenanceTask.getItemId());
        maintenanceTask.setPeriod(maintenanceTask.getPeriod());
        return dto;
    }
}
