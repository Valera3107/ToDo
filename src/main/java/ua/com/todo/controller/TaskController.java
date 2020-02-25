package ua.com.todo.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.dto.TaskCreateDto;
import ua.com.todo.dto.TaskDto;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {

  private final TaskService taskServiceImpl;
  private final ModelMapper mapper;

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
  public ResponseEntity<List<TaskDto>> save(@RequestBody @Valid TaskCreateDto taskDto) {
    taskServiceImpl.save(convertToCreateEntity(taskDto));
    return ResponseEntity.ok(taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  @GetMapping("/statistic/{date}/{num}")
  public List<TaskDto> finishedTask(@PathVariable String date, @PathVariable String num) {
    return taskServiceImpl.getStatistic(date, num).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  public List<TaskDto> update(@PathVariable Long id, @RequestBody @Valid TaskCreateDto taskDto) {
    Task task = convertToCreateEntity(taskDto);
    return taskServiceImpl.update(task, id).stream().map(this::convertToDto).collect(Collectors.toList());
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

  private TaskDto convertToDto(Task task) {
    return mapper.map(task, TaskDto.class);
  }

  private Task convertToCreateEntity(TaskCreateDto taskCreateDto) {
    return mapper.map(taskCreateDto, Task.class);
  }

}
