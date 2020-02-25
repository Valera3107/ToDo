package ua.com.todo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.dto.TaskDto;
import ua.com.todo.model.Essence;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;
import ua.com.todo.model.ErrorMessage;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
public class TaskController {

  private final TaskService taskServiceImpl;

  private final ModelMapper mapper;

  public TaskController(TaskService taskService, ModelMapper mapper) {
    this.taskServiceImpl = taskService;
    this.mapper = mapper;
  }

  @GetMapping
  public List<TaskDto> list() {
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public TaskDto getTaskById(@PathVariable Long id) {
    return convertToDto(taskServiceImpl.getById(id));
  }

  @GetMapping("/filter/{name}")
  public List<TaskDto> getTaskByName(@PathVariable("name") String name) {
    return taskServiceImpl.getByName(name).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public List<? extends Essence> save(@RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return getErrorList(bindingResult);
    } else {
      taskServiceImpl.save(convertToEntity(taskDto));
    }
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/statistic/{date}/{num}")
  public List<TaskDto> finishedTask(@PathVariable String date, @PathVariable String num) {
    return taskServiceImpl.getStatistic(date, num).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  public List<? extends Essence> update(@PathVariable Long id, @RequestBody @Valid TaskDto taskDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return getErrorList(bindingResult);
    } else {
      Task task = convertToEntity(taskDto);
      return taskServiceImpl.update(task, id).stream().map(this::convertToDto).collect(Collectors.toList());
    }
  }

  @PutMapping("/{id}/{status}")
  public List<TaskDto> changeStatus(@PathVariable Long id, @PathVariable String status) {
    taskServiceImpl.changeStatus(id, status);
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @DeleteMapping("/{id}")
  public List<TaskDto> delete(@PathVariable Long id) {
    taskServiceImpl.delete(id);
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private List<? extends Essence> getErrorList(BindingResult bindingResult) {
    Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
    return errors.entrySet().stream().map(e -> new ErrorMessage(new Date(), e.getKey(), e.getValue())).collect(Collectors.toList());
  }

  private TaskDto convertToDto(Task task) {
    return mapper.map(task, TaskDto.class);
  }

  private Task convertToEntity(TaskDto taskDto) {
    return mapper.map(taskDto, Task.class);
  }
}
