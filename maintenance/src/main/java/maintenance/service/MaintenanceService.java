package maintenance.service;

import maintenance.domain.dto.MaintenanceTaskDTO;
import maintenance.domain.model.BusinessPeriod;
import maintenance.domain.model.MaintenanceTask;
import maintenance.domain.repository.MaintenanceTaskRepository;
import maintenance.infrastructure.IDGenerator;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by erdem on 16.05.17.
 */
@Service
public class MaintenanceService {
    @Autowired
    MaintenanceTaskRepository repo;
    @Autowired
    MaintenanceTaskAssembler assembler;

    @Value("main_url")
    String base_url;

    public MaintenanceTaskDTO createTask(MaintenanceTaskDTO taskDTO) {
        MaintenanceTask task = MaintenanceTask.of(IDGenerator.generate(), taskDTO.getItemId(), taskDTO.getBusinessPeriod());
        repo.save(task);
        notifyMaintenance(taskDTO);
        return assembler.toResource(task);
    }

    private void notifyMaintenance(MaintenanceTaskDTO taskDTO) {

    }

    public boolean isAvailable(String plantId, LocalDate startDate, LocalDate endDate) {
        BusinessPeriod schedule = BusinessPeriod.of(startDate, endDate);
        return  !repo.findAllByItemId(plantId)
                .stream()
                .anyMatch(maintenanceTask -> maintenanceTask.getPeriod().isIntersectingWith(schedule));
    }

    public MaintenanceTaskDTO findTask(String taskId) {
        return assembler.toResource(repo.findOne(taskId));
    }

    public List<MaintenanceTaskDTO> findAll() {
        return assembler.toResources(repo.findAll());
    }
}
