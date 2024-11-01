package ws.task.tasklist.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.dto.task.TaskDto;
import ws.task.tasklist.Entity.dto.validation.OnUpdate;
import ws.task.tasklist.Service.Mappers.TaskMapper;
import ws.task.tasklist.Service.TaskService;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;



    @PutMapping
    public TaskDto update(@Validated(OnUpdate.class) @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        taskService.delete(id);
        return new ResponseEntity<>("Task with id" + id + "was deleted", HttpStatus.GONE);
    }

}
