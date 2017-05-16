package maintenance.service;

import maintenance.domain.dto.MaintenanceTaskDTO;
import maintenance.domain.model.MaintenanceTask;
import maintenance.domain.repository.MaintenanceTaskRepository;
import maintenance.infrastructure.IDGenerator;
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

    public List<MaintenanceTaskDTO> queryTasks(String plantId) {
      return  repo.findAllByItemId(plantId)
                .stream()
                .filter(maintenanceTask -> maintenanceTask.getPeriod().getEndDate().isAfter(LocalDate.now()))
                .map(assembler::toResource)
                .collect(toList());
    }
}
