package ws.task.tasklist.Service.Mappers;

import lombok.SneakyThrows;
import ws.task.tasklist.Entity.Role;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.User;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserRowMapper {

    @SneakyThrows
    public static User mapRow(ResultSet resultSet) {
        Set<Role> roles = new HashSet<>();
        while (resultSet.next()) {
            roles.add(Role.valueOf(resultSet.getString("user_roles_role")));
        }
        resultSet.beforeFirst();
        List<Task> taskList = TaskRowMapper.mapRows(resultSet);
        resultSet.beforeFirst();
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("user_id"));
            user.setName(resultSet.getString("user_name"));
            user.setUsername(resultSet.getString("user_username"));
            user.setPassword(resultSet.getString("user_password"));
            user.setRoles(roles);
            user.setTasks(taskList);
            return user;
        }
        return null;
    }
}
