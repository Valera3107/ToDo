package ua.com.todo.todo.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ua.com.todo.todo.model.Status;
import ua.com.todo.todo.model.Task;
import ua.com.todo.todo.repository.TaskRepository;
import ua.com.todo.todo.service.TaskService;

@Component
public class TaskCommandLineRunner implements CommandLineRunner {

  private final TaskService taskService;

  public TaskCommandLineRunner(TaskService taskService) {
    this.taskService = taskService;
  }

  @Override
  public void run(String... args) {
    taskService.save(new Task("Rest", "Drink a cup of tea", 2, Status.NEW));
    taskService.save(new Task("Rest", "Drink a cup of coffee", 3, Status.NEW));
    taskService.save(new Task("Code review", "Check and fix all application working", 8, Status.NEW));
    taskService.save(new Task("Make new method", "Write new method for more functionality", 6, Status.NEW));
    taskService.save(new Task("Sleep", "Take a sleep", 10, Status.NEW));
  }
}
