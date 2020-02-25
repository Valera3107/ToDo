package ua.com.todo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ua.com.todo.model.Essence;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class TaskDto implements Essence {

  @Null
  private Long id;

  @NonNull
  @Size(min = 2, max = 50, message = "Name must be between 3 and 50 characters!")
  private String name;

  @NonNull
  @Size(min = 10, max = 250, message = "Description must be between 10 and 250 characters!")
  private String description;

  @NonNull
  @Min(value = 1, message = "Importance field must be between 1 and 10!")
  @Max(value = 10, message = "Importance field must be between 1 and 10!")
  private Integer importance;

  @Column(updatable = false)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime startTask;

  @Column
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime finishTask;
}
