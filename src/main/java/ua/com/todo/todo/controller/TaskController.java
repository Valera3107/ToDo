package ua.com.todo.todo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.todo.model.ErrorMessage;
import ua.com.todo.todo.model.Essence;
import ua.com.todo.todo.model.Task;
import ua.com.todo.todo.service.TaskService;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskServiceImpl;

  public TaskController(TaskService taskService) {
    this.taskServiceImpl = taskService;
  }

  @GetMapping
  public List<Task> list() {
    return taskServiceImpl.getAll();
  }

  @GetMapping("/{id}")
  public Task getTaskById(@PathVariable Long id) {
    return taskServiceImpl.getById(id);
  }

  @GetMapping("/filter/{name}")
  public List<Task> getTaskByName(@PathVariable("name") String name) {
    return taskServiceImpl.getByName(name);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public List<? extends Essence> save(@RequestBody @Valid Task task, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return getErrorList(bindingResult);
    } else {
      taskServiceImpl.save(task);
    }
    return taskServiceImpl.getAll();
  }

  @GetMapping("/statistic/{date}/{num}")
  public List<Task> finishedTask(@PathVariable String date, @PathVariable String num) {
    return taskServiceImpl.getStatistic(date, num);
  }

  @PutMapping("/{id}")
  public List<? extends Essence> update(@PathVariable Long id, @RequestBody @Valid Task task, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return getErrorList(bindingResult);
    } else {
      return taskServiceImpl.update(task, id);
    }
  }

  @PutMapping("/{id}/{status}")
  public List<Task> changeStatus(@PathVariable Long id, @PathVariable String status) {
    taskServiceImpl.changeStatus(id, status);
    return taskServiceImpl.getAll();
  }

  @DeleteMapping("/{id}")
  public List<Task> delete(@PathVariable Long id) {
    taskServiceImpl.delete(id);
    return taskServiceImpl.getAll();
  }

  private List<? extends Essence> getErrorList(BindingResult bindingResult) {
    Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
    return errors.entrySet().stream().map(e -> new ErrorMessage(new Date(), e.getKey(), e.getValue())).collect(Collectors.toList());
  }
}
