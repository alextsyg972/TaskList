package ws.task.tasklist.Service.mappers;

import org.mapstruct.Mapper;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Entity.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper extends Mappable<User, UserDto> {
}