package ws.task.tasklist.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ws.task.tasklist.Entity.Task;

import java.sql.Timestamp;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = """
            SELECT * from tasks t
            join users_tasks ut ON ut.task_id = t.id
            where ut.user_id = :userId
            """, nativeQuery = true)
    List<Task> findAllByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = """
            INSERT INTO users_tasks (user_id, task_id)
            VALUES (:userId, :taskId)
            """, nativeQuery = true)
    void assignTask(
            @Param("userId") Long userId,
            @Param("taskId") Long taskId
    );

    @Modifying
    @Query(value = """
            INSERT INTO tasks_images (task_id, image)
            VALUES (:id, :fileName)
            """, nativeQuery = true)
    void addImage(
            @Param("id") Long id,
            @Param("fileName") String fileName
    );

    @Query(value = """
            Select * from tasks t 
            WHERE t.expiration_date is not null
            and t.expiration_date between :start and :end
            """, nativeQuery = true)
    List<Task> findAllSoonTasks(@Param("start")Timestamp timestamp,
                                @Param("end")Timestamp end);

}