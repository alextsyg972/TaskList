package ws.task.tasklist.Service.Mappers;

import org.mapstruct.Mapper;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.dto.task.TaskDto;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskDto toDto(Task task);

    List<TaskDto> toDto(List<Task> tasks);

    Task toEntity(TaskDto taskDto);
}
