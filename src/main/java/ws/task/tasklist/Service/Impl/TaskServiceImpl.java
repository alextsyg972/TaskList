package ws.task.tasklist.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.task.tasklist.Entity.Status;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.TaskImage;
import ws.task.tasklist.Exception.ResourceNotFoundException;
import ws.task.tasklist.Repository.TaskRepository;
import ws.task.tasklist.Service.ImageService;
import ws.task.tasklist.Service.TaskService;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ImageService imageService;

    @Override
    @Cacheable(value = "TaskService::getById", key = "#id")
    public Task getById(final Long id) {


        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found."));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllSoonTasks(Duration duration) {
        LocalDateTime now = LocalDateTime.now();
        return taskRepository.findAllSoonTasks(Timestamp.valueOf(now),Timestamp.valueOf(now.plus(duration)));
    }

    @Override
    public List<Task> getAllByUserId(Long id) {
        return taskRepository.findAllByUserId(id);
    }

    @Override
    @CachePut(value = "TaskService::getById", key = "#task.id")
    public Task update(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        return task;
    }

    @Override
    @Cacheable(value = "TaskService::getById", condition = "#task.id!=null", key = "#task.id")
    public Task create(Task task, Long userId) {
        if (task.getStatus() == null) {
            task.setStatus(Status.TODO);
        }
        taskRepository.save(task);
        taskRepository.assignTask(userId, task.getId());
        return task;
    }

    @Override
    @CacheEvict(value = "TaskService::getById", key = "#id")
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    @CacheEvict(value = "TaskService::getById", key = "#id")
    @Override
    public void uploadImage(Long id, TaskImage image) {
        String fileName = imageService.upload(image);
        taskRepository.addImage(id, fileName);
    }
}
