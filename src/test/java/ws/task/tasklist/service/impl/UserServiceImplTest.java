package ws.task.tasklist.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ws.task.tasklist.Entity.MailType;
import ws.task.tasklist.Entity.Role;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Exception.ResourceNotFoundException;
import ws.task.tasklist.Repository.UserRepository;
import ws.task.tasklist.Service.Impl.MailServiceImpl;
import ws.task.tasklist.Service.Impl.UserServiceImpl;
import ws.task.tasklist.config.TestConfig;

import java.util.Optional;
import java.util.Properties;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private MailServiceImpl mailService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void getById() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.of(user));
        User testUser = userService.getById(id);
        Mockito.verify(userRepository).findById(id);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingId() {
        Long id = 1L;
        Mockito.when(userRepository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(id));
        Mockito.verify(userRepository).findById(id);
    }

    @Test
    void getByUsername() {
        String username = "username@gmail.com";
        User user = new User();
        user.setUsername(username);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(user));
        User testUser = userService.getByUsername(username);
        Mockito.verify(userRepository).findByUsername(username);
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void getByNotExistingUsername() {
        String username = "username@gmail.com";
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class,
                () -> userService.getByUsername(username));
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void update() {
        Long id = 1L;
        String password = "password";
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Mockito.when(userRepository.findById(user.getId()))
                .thenReturn(Optional.of(user));
        User updated = userService.update(user);
        Mockito.verify(passwordEncoder).encode(password);
        Mockito.verify(userRepository).save(user);
        Assertions.assertEquals(user.getUsername(), updated.getUsername());
        Assertions.assertEquals(user.getName(), updated.getName());
    }

    @Test
    void isTaskOwner() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(true);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertTrue(isOwner);
    }

    @Test
    void create() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        User testUser = userService.create(user);
        Mockito.verify(userRepository).save(user);
        Mockito.verify(mailService).sendEmail(user,
                MailType.REGISTRATION,
                new Properties());
        Assertions.assertEquals(Set.of(Role.role_user), testUser.getRoles());
        Assertions.assertEquals("encodedPassword",
                testUser.getPassword());
    }

    @Test
    void createWithExistingUsername() {
        String username = "username@gmail.com";
        String password = "password";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(password);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.of(new User()));
        Mockito.when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void createWithDifferentPasswords() {
        String username = "username@gmail.com";
        String password = "password1";
        String passwordConfirmation = "password2";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPasswordConfirmation(passwordConfirmation);
        Mockito.when(userRepository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalStateException.class,
                () -> userService.create(user));
        Mockito.verify(userRepository, Mockito.never()).save(user);
    }

    @Test
    void isTaskOwnerWithFalse() {
        Long userId = 1L;
        Long taskId = 1L;
        Mockito.when(userRepository.isTaskOwner(userId, taskId))
                .thenReturn(false);
        boolean isOwner = userService.isTaskOwner(userId, taskId);
        Mockito.verify(userRepository).isTaskOwner(userId, taskId);
        Assertions.assertFalse(isOwner);
    }

    @Test
    void getTaskAuthor() {
        Long taskId = 1L;
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        Mockito.when(userRepository.findTaskAuthor(taskId))
                .thenReturn(Optional.of(user));
        User author = userService.getTaskAuthor(taskId);
        Mockito.verify(userRepository).findTaskAuthor(taskId);
        Assertions.assertEquals(user, author);
    }

    @Test
    void getNotExistingTaskAuthor() {
        Long taskId = 1L;
        Mockito.when(userRepository.findTaskAuthor(taskId))
                .thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () ->
                userService.getTaskAuthor(taskId));
        Mockito.verify(userRepository).findTaskAuthor(taskId);
    }

    @Test
    void delete() {
        Long id = 1L;
        userService.delete(id);
        Mockito.verify(userRepository).deleteById(id);
    }
}