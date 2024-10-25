package ws.task.tasklist.Service.Mappers;

import org.mapstruct.Mapper;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Entity.dto.user.UserDto;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toEntity(UserDto dto);

}
