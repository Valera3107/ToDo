package ua.com.todo.validation.impl;

import ua.com.todo.model.Status;
import ua.com.todo.validation.interafaces.ValidTaskStatus;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class TaskStatusValidator implements ConstraintValidator<ValidTaskStatus, String> {
  @Override
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    try {
      Status.valueOf(s);
      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
