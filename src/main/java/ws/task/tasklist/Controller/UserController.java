package ws.task.tasklist.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Entity.dto.task.TaskDto;
import ws.task.tasklist.Entity.dto.user.UserDto;
import ws.task.tasklist.Entity.dto.validation.OnCreate;
import ws.task.tasklist.Entity.dto.validation.OnUpdate;
import ws.task.tasklist.Service.Mappers.TaskMapper;
import ws.task.tasklist.Service.Mappers.UserMapper;
import ws.task.tasklist.Service.TaskService;
import ws.task.tasklist.Service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final TaskService taskService;

    private final UserMapper userMapper;
    private final TaskMapper taskMapper;



    @GetMapping("/{id}")
    public UserDto getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return userMapper.toDto(user);
    }

    @PutMapping
    public UserDto update(@Validated(OnUpdate.class) @RequestBody UserDto dto) {
        User user = userMapper.toEntity(dto);
        User updatedUser = userService.update(user);
        return userMapper.toDto(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>("user with id " + id + "was deleted", HttpStatus.GONE);
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDto> getTasksById(@PathVariable Long id) {
        List<Task> tasks = taskService.getAllByUserId(id);
        return taskMapper.toDto(tasks);
    }

    @PostMapping("/{id}/tasks")
    public TaskDto createTask(@PathVariable Long id,
                              @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task createdTask = taskService.create(task,id);
        return taskMapper.toDto(createdTask);
    }

}
