package ws.task.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ws.task.tasklist.Entity.Status;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.TaskImage;
import ws.task.tasklist.Exception.ResourceNotFoundException;
import ws.task.tasklist.Repository.TaskRepository;
import ws.task.tasklist.Service.ImageService;
import ws.task.tasklist.Service.Impl.TaskServiceImpl;
import ws.task.tasklist.config.TestConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ImageService imageService;

    @Autowired
    private TaskServiceImpl taskService;

    @Test
    void getById() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.getById(id);
        Mockito.verify(taskRepository).findById(id);
        Assertions.assertEquals(task, testTask);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(taskRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> taskService.getById(id));
        Mockito.verify(taskRepository).findById(id);
    }

    @Test
    void getAllByUserId() {
        Long userId = 1L;
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllByUserId(userId))
                .thenReturn(tasks);
        List<Task> testTasks = taskService.getAllByUserId(userId);
        Mockito.verify(taskRepository).findAllByUserId(userId);
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void getSoonTasks() {
        Duration duration = Duration.ofHours(1);
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            tasks.add(new Task());
        }
        Mockito.when(taskRepository.findAllSoonTasks(Mockito.any(), Mockito.any()))
                .thenReturn(tasks);
        List<Task> testTasks = taskService.getAllSoonTasks(duration);
        Mockito.verify(taskRepository)
                .findAllSoonTasks(Mockito.any(), Mockito.any());
        Assertions.assertEquals(tasks, testTasks);
    }

    @Test
    void update() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        task.setStatus(Status.DONE);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task, testTask);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(
                task.getDescription(),
                testTask.getDescription()
        );
        Assertions.assertEquals(task.getStatus(), testTask.getStatus());
    }

    @Test
    void updateWithEmptyStatus() {
        Long id = 1L;
        Task task = new Task();
        task.setId(id);
        Mockito.when(taskRepository.findById(task.getId()))
                .thenReturn(Optional.of(task));
        Task testTask = taskService.update(task);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertEquals(task.getTitle(), testTask.getTitle());
        Assertions.assertEquals(
                task.getDescription(),
                testTask.getDescription()
        );
        Assertions.assertEquals(testTask.getStatus(), Status.TODO);
    }

    @Test
    void create() {
        Long userId = 1L;
        Long taskId = 1L;
        Task task = new Task();
        Mockito.doAnswer(invocation -> {
                    Task savedTask = invocation.getArgument(0);
                    savedTask.setId(taskId);
                    return savedTask;
                })
                .when(taskRepository).save(task);
        Task testTask = taskService.create(task, userId);
        Mockito.verify(taskRepository).save(task);
        Assertions.assertNotNull(testTask.getId());
        Mockito.verify(taskRepository).assignTask(userId, task.getId());
    }

    @Test
    void delete() {
        Long id = 1L;
        taskService.delete(id);
        Mockito.verify(taskRepository).deleteById(id);
    }

    @Test
    void uploadImage() {
        Long id = 1L;
        String imageName = "imageName";
        TaskImage taskImage = new TaskImage();
        Mockito.when(imageService.upload(taskImage))
                .thenReturn(imageName);
        taskService.uploadImage(id, taskImage);
        Mockito.verify(taskRepository).addImage(id, imageName);
    }
}
