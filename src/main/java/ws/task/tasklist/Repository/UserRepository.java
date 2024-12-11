package ws.task.tasklist.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ws.task.tasklist.Entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @Query(value = """
            SELECT exists(
                SELECT 1
                FROM users_tasks
                WHERE user_id = :userId
                AND task_id = :taskId)
            """, nativeQuery = true)
    boolean isTaskOwner(@Param("userId") Long userId, @Param("taskId") Long taskId);

    @Query(value = """
            SELECT u.id as id,
                   u.name as name,
                   u.username as password
            from users_tasks
            JOIN users u on ut.user_id = u.id
            where ut.task_id = :taskId
            """, nativeQuery = true)
    Optional<User> findTaskAuthor(@Param("taskId") Long taskId);
}
