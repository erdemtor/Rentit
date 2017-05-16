package maintenance.controller;

import maintenance.domain.dto.MaintenanceTaskDTO;
import maintenance.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    @Autowired
    MaintenanceService maintenanceService;

    @PostMapping
    MaintenanceTaskDTO createTask(@RequestBody MaintenanceTaskDTO task){
       return maintenanceService.createTask(task);
    }

    @GetMapping("/{plantId}")
    List<MaintenanceTaskDTO> getPlantsTasks(@PathVariable String plantId){
        return maintenanceService.queryTasks(plantId);
    }
}
