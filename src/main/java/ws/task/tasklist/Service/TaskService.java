package ws.task.tasklist.Service;

import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.TaskImage;

import java.util.List;

public interface TaskService {

    Task getById(Long id);

    List<Task> getAllByUserId(Long userId);

    Task update(Task task);

    Task create(Task task, Long userId);

    void delete(Long id);

    void uploadImage(Long taskId, TaskImage image);

}
