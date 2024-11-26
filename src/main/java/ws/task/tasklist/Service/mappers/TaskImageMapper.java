package ws.task.tasklist.Service.mappers;

import org.mapstruct.Mapper;
import ws.task.tasklist.Entity.TaskImage;
import ws.task.tasklist.Entity.dto.task.TaskImageDto;

@Mapper(componentModel = "spring")
public interface TaskImageMapper extends Mappable<TaskImage, TaskImageDto> {
}
