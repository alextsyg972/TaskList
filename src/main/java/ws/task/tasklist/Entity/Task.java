package ws.task.tasklist.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tasks")
@Data
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    private LocalDateTime expirationDate;

    @Column(name = "image")
    @CollectionTable(name = "task_images")
    @ElementCollection
    private List<String> images;
}
