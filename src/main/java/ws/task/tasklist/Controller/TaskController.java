package ws.task.tasklist.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.TaskImage;
import ws.task.tasklist.Entity.dto.task.TaskDto;
import ws.task.tasklist.Entity.dto.task.TaskImageDto;
import ws.task.tasklist.Entity.dto.validation.OnUpdate;
import ws.task.tasklist.Service.TaskService;
import ws.task.tasklist.Service.mappers.TaskImageMapper;
import ws.task.tasklist.Service.mappers.TaskMapper;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Validated
@Tag(name = "Task controller", description = "Task API")

public class TaskController {

    private final TaskService taskService;

    private final TaskMapper taskMapper;

    private final TaskImageMapper taskImageMapper;


    @PutMapping
    @MutationMapping(name = "updateTask")
    @Operation(summary = "Update Task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#dto.id)")
    public TaskDto update(@Validated(OnUpdate.class) @Argument @RequestBody TaskDto dto) {
        Task task = taskMapper.toEntity(dto);
        Task updatedTask = taskService.update(task);
        return taskMapper.toDto(updatedTask);
    }

    @GetMapping("/{id}")
    @QueryMapping(name = "taskById")
    @Operation(summary = "Get Task by id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public TaskDto getById(@PathVariable @Argument Long id) {
        Task task = taskService.getById(id);
        return taskMapper.toDto(task);
    }

    @DeleteMapping("/{id}")
    @MutationMapping(name = "deleteTask")
    @Operation(summary = "Delete Task by id")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public ResponseEntity<String> deleteById(@PathVariable @Argument Long id) {
        taskService.delete(id);
        return new ResponseEntity<>("Task with id" + id + "was deleted", HttpStatus.GONE);
    }

    @PostMapping("/{id}/image")
    @Operation(summary = "Upload image to task")
    @PreAuthorize("@customSecurityExpression.canAccessTask(#id)")
    public void uploadImage(@PathVariable Long id,
                            @Validated @ModelAttribute TaskImageDto imageDto) {
        TaskImage taskImage = taskImageMapper.toEntity(imageDto);
        taskService.uploadImage(id, taskImage);
    }

}
