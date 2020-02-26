package ua.com.todo.validation.impl;

import ua.com.todo.model.Status;
import ua.com.todo.validation.interafaced.ValidTaskStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TaskStatusValidator implements ConstraintValidator<ValidTaskStatus, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    if (s.isEmpty()) return false;
    Status.valueOf(s);
    return true;
  }
}
