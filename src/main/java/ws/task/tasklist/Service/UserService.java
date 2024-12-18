package ws.task.tasklist.Service;

import ws.task.tasklist.Entity.User;

public interface UserService {

    User getById(Long id);

    User getByUsername(String username);

    User update(User user);

    User create(User user);

    boolean isTaskOwner(Long userId, Long taskId);

    void delete(Long userId);

    User getTaskAuthor(Long taskId);

}
