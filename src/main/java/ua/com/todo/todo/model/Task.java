package ua.com.todo.todo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@ToString(of = {"id", "name"})
public class Task {

  @Id
  @GeneratedValue
  private Long id;

  @NonNull
  @NotNull(message = "Name field must not be null!")
  private String name;

  @NonNull
  @NotNull(message = "Description field must not be null!")
  @Size(min=10, max=250, message = "Description must be between 10 and 250 characters!")
  private String description;

  @NonNull
  @Min(value = 1, message = "Importance field must be between 1 and 10!")
  @Max(value = 10, message = "Importance field must be between 1 and 10!")
  private Integer importance;

  @NonNull
  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startTask;

  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime finishTask;
}
