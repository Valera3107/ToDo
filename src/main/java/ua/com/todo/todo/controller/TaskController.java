package ua.com.todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.todo.model.Task;
import ua.com.todo.todo.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public List<Task> list() {
    return taskService.getAll();
  }

  @GetMapping("/{id}")
  public Task getTaskById(@PathVariable("id") String taskId) {
    return taskService.getById(taskId);
  }

  @GetMapping("/filter/{name}")
  public List<Task> getTaskByName(@PathVariable("name") String name) {
    return taskService.getByName(name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public List<Task> save(@RequestBody Task task) {
    taskService.save(task);
    return taskService.getAll();
  }

  @GetMapping("/statistic/{date}/{num}")
  public List<Task> finishedTask(@PathVariable String date, @PathVariable String num){
    return taskService.getStatistic(date, num);
  }

  @PutMapping
  public void update(@RequestBody Task task) {
    taskService.update(task);
  }

  @PutMapping("/{id}/{status}")
  public List<Task> changeStatus(@PathVariable String id, @PathVariable String status){
    taskService.changeStatus(id, status);
    return taskService.getAll();
  }

  @DeleteMapping("/{id}")
  public List<Task> delete(@PathVariable String id) {
    taskService.delete(id);
    return taskService.getAll();
  }
}
