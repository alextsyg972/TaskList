package ws.task.tasklist.Service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ws.task.tasklist.Entity.MailType;
import ws.task.tasklist.Entity.Task;
import ws.task.tasklist.Entity.User;
import ws.task.tasklist.Service.MailService;
import ws.task.tasklist.Service.Reminder;
import ws.task.tasklist.Service.TaskService;
import ws.task.tasklist.Service.UserService;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class ReminderImpl implements Reminder {

    private final TaskService taskService;
    private final UserService userService;
    private final MailService mailService;
    private final Duration DURATION = Duration.ofHours(1);

    @Scheduled(cron = "0 * * * * *")
    @Override
    public void remindForTask() {
        List<Task> taskList = taskService.getAllSoonTasks(DURATION);
        taskList.forEach(task -> {
            User user = userService.getTaskAuthor(task.getId());
            Properties properties = new Properties();
            properties.setProperty("task.title", task.getTitle());
            properties.setProperty("task.description", task.getDescription());
            mailService.sendEmail(user, MailType.REMINDER, properties);
        });
    }
}
