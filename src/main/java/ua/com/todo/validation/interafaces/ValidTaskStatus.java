package ua.com.todo.validation.interafaces;

import ua.com.todo.validation.impl.TaskStatusValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TaskStatusValidator.class)
@Target( { ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTaskStatus {
  String message() default "Invalid status name";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
}
