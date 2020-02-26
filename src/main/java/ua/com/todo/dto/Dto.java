package ua.com.todo.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dto {
  @Size(min = 2, max = 50, message = "Name must be between 3 and 50 characters!")
  private String name;

  @Size(min = 10, max = 250, message = "Description must be between 10 and 250 characters!")
  private String description;

  @Min(value = 1, message = "Importance field must be between 1 and 10!")
  @Max(value = 10, message = "Importance field must be between 1 and 10!")
  private Integer importance;
}
