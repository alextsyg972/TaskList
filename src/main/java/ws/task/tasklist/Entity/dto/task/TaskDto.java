package ws.task.tasklist.Entity.dto.task;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ws.task.tasklist.Entity.Status;
import ws.task.tasklist.Entity.dto.validation.OnCreate;
import ws.task.tasklist.Entity.dto.validation.OnUpdate;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TaskDto {
    @NotNull(message = "id must be not null", groups = OnUpdate.class)
    private Long id;
    @NotNull(message = "title must be not null", groups = {OnCreate.class, OnUpdate.class})
    @Length(max = 255, message = "Title length must be less than 255 symbols")
    private String title;
    @Length(max = 255, message = "Title length must be less than 255 symbols")
    private String description;

    private Status status;
    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime expirationDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> images;

}
