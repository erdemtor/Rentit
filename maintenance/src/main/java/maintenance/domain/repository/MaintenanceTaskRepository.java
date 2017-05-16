package maintenance.domain.repository;

import maintenance.domain.model.MaintenanceTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by erdem on 16.05.17.
 */
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask, String> {

    List<MaintenanceTask> findAllByItemId(String itemId);
}
