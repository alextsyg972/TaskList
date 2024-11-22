package ws.task.tasklist.Service.mappers;


import org.mapstruct.Mapper;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.dto.task.TaskDto;

@Mapper(componentModel = "spring")
public interface TaskMapper extends Mappable<Task, TaskDto> {
}