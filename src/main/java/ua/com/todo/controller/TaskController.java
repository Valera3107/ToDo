package ua.com.todo.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.dto.Dto;
import ua.com.todo.dto.TaskDto;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;
import ua.com.todo.validation.interafaces.ValidTaskStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
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
  public List<TaskDto> save(@RequestBody @Valid Dto taskDto) {
    taskServiceImpl.save(convertToCreateEntity(taskDto));
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/statistic/{date}/{num}")
  public List<TaskDto> getStatistic(@PathVariable String date, @PathVariable Integer num) {
    return taskServiceImpl.getStatistic(date, num).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  public TaskDto update(@PathVariable Long id, @RequestBody @Valid Dto taskDto) {
    Task task = convertToCreateEntity(taskDto);
    return convertToDto(taskServiceImpl.update(task, id));
  }

  @PutMapping("/{id}/{status}")
  public TaskDto changeStatus(@PathVariable Long id, @ValidTaskStatus @PathVariable String status) {
    return convertToDto(taskServiceImpl.changeStatus(id, status));
  }

  @DeleteMapping("/{id}")
  public List<TaskDto> delete(@PathVariable Long id) {
    taskServiceImpl.delete(id);
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  private TaskDto convertToDto(Task task) {
    return mapper.map(task, TaskDto.class);
  }

  private Task convertToCreateEntity(Dto dto) {
    return mapper.map(dto, Task.class);
  }

}
