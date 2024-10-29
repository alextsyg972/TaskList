package ws.task.tasklist.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserService userService;

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return null;
    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public boolean isTaskOwner(Long userId, Long taskId) {
        return false;
    }

    @Override
    public void delete(Long userId) {

    }
}
