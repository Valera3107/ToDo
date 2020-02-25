package ua.com.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString(of = {"id", "name"})
public class Task implements Essence {

  @Id
  @GeneratedValue
  private Long id;

  @NonNull
  private String name;

  @NonNull
  private String description;

  @NonNull
  private Integer importance;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startTask;

  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime finishTask;

  public void clone(Task task) {
    this.name = task.getName();
    this.description = task.getDescription();
    this.importance = task.getImportance();
    this.finishTask = task.getFinishTask();
    this.status = task.getStatus();
  }
}
