package ws.task.tasklist.Service;

import ws.task.tasklist.Entity.MailType;
import ws.task.tasklist.Entity.User;

import java.util.Properties;

public interface MailService {
    void sendEmail(User user, MailType type, Properties params);

}
