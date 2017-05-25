package maintenance.controller;

import maintenance.domain.dto.MaintenanceTaskDTO;
import maintenance.service.MaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */

@RestController
@RequestMapping("/api/maintenance")
@CrossOrigin
public class MaintenanceController {
    @Autowired
    MaintenanceService maintenanceService;

    @PostMapping("/")
    MaintenanceTaskDTO createTask(@RequestBody MaintenanceTaskDTO task){

       return maintenanceService.createTask(task);
    }

    @GetMapping("/{taskId}")
    MaintenanceTaskDTO findTask(@PathVariable String taskId){
        return maintenanceService.findTask(taskId);
    }

    @GetMapping("/task")
    List<MaintenanceTaskDTO> findTask(){
        return maintenanceService.findAll();
    }



    @GetMapping("/plants/{plantId}")
    boolean getPlantsTasks(@PathVariable String plantId,
                                            @RequestParam(name = "startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam(name = "endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return maintenanceService.isAvailable(plantId, startDate, endDate);
    }
}
