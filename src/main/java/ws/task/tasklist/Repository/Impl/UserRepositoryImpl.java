package ws.task.tasklist.Repository.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ws.task.tasklist.Entity.Role;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Exception.ResourceMappingException;
import ws.task.tasklist.Repository.DataSourceConfig;
import ws.task.tasklist.Repository.UserRepository;
import ws.task.tasklist.Service.Mappers.UserRowMapper;

import javax.swing.text.html.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final DataSourceConfig dataSourceConfig;

    private final String FIND_BY_ID = """
            select u.id       as user_id,
                   u.name     as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_roles_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            from users u
                     left join users_roles ur on u.id = ur.user_id
                     left join public.users_tasks ut on u.id = ut.user_id
                     left join public.tasks t on ut.task_id = t.id
            where u.id = ?
            """;
    private final String FIND_BY_USERNAME = """
            select u.id       as user_id,
                   u.name     as user_name,
                   u.username as user_username,
                   u.password as user_password,
                   ur.role as user_roles_role,
                   t.id as task_id,
                   t.title as task_title,
                   t.description as task_description,
                   t.expiration_date as task_expiration_date,
                   t.status as task_status
            from users u
                     left join users_roles ur on u.id = ur.user_id
                     left join public.users_tasks ut on u.id = ut.user_id
                     left join public.tasks t on ut.task_id = t.id
            where u.username = ?
            """;

    private final String UPDATE = """
            UPDATE users
            SET name = ?
                username = ?
                password = ?
            WHERE id = ?
            """;
    private final String CREATE = """
            INSERT INTO users (name, username, password)
            VALUES (?, ?, ?)
            """;
    private final String INSERTT_USER_ROLE = """
            INSERT INTO users_roles (user_id, role)
            VALUES (?, ?)
            """;

    private final String IS_TASK_OWER = """
            SELECT EXISTS(
                SELECT 1
                FROM users_tasks
                WHERE user_id = ?
                AND task_id = ?
            )
            """;

    private final String DELETE = """
            DELETE FROM users
            WHERE id = ?
            """;


    @Override
    public Optional<User> findById(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_ID,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding user by id");
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(FIND_BY_USERNAME,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                return Optional.ofNullable(UserRowMapper.mapRow(rs));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Error while finding user by username");
        }
    }

    @Override
    public void update(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setLong(4, user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while updating user");
        }
    }

    @Override
    public void create(User user) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                resultSet.next();
                user.setId(resultSet.getLong(1));
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while creating user");
        }
    }

    @Override
    public void insertUserRole(Long userId, Role role) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(INSERTT_USER_ROLE);
            statement.setLong(1, userId);
            statement.setString(2, role.name());
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while inserting user role");
        }
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        try  {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(IS_TASK_OWER);
            statement.setLong(1, userId);
            statement.setLong(1, taskId);
            statement.executeQuery();
            try (ResultSet resultSet = statement.executeQuery()) {
                resultSet.next();
                return resultSet.getBoolean(1);
            }
        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while checking if user is task owner");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            Connection connection = dataSourceConfig.getConnection();
            PreparedStatement statement = connection.prepareStatement(DELETE);
            statement.setLong(1, id);
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new ResourceMappingException("Exception while deleting user");
        }
    }
}
