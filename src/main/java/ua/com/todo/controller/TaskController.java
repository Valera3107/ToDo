package ua.com.todo.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.dto.Dto;
import ua.com.todo.dto.TaskDto;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;

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
  public ResponseEntity<List<TaskDto>> list() {
    return ResponseEntity.ok(taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable Long id) {
    return ResponseEntity.ok(convertToDto(taskServiceImpl.getById(id)));
  }

  @GetMapping("/filter/{name}")
  public ResponseEntity<List<TaskDto>> getTaskByName(@PathVariable("name") String name) {
    return ResponseEntity.ok(taskServiceImpl.getByName(name).stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<List<TaskDto>> save(@RequestBody @Valid Dto taskDto) {
    taskServiceImpl.save(convertToCreateEntity(taskDto));
    return ResponseEntity.ok(taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  @GetMapping("/statistic/{date}/{num}")
  public ResponseEntity<List<TaskDto>> getStatistic(@PathVariable String date, @PathVariable Integer num) {
    return ResponseEntity.ok(taskServiceImpl.getStatistic(date, num).stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskDto> update(@PathVariable Long id, @RequestBody @Valid Dto taskDto) {
    Task task = convertToCreateEntity(taskDto);
    return ResponseEntity.ok(convertToDto(taskServiceImpl.update(task, id)));
  }

  @PutMapping("/{id}/{status}")
  public ResponseEntity<TaskDto> changeStatus(@PathVariable Long id, @PathVariable @Valid String status) {
    return ResponseEntity.ok(convertToDto(taskServiceImpl.changeStatus(id, status)));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<List<TaskDto>> delete(@PathVariable Long id) {
    taskServiceImpl.delete(id);
    return ResponseEntity.ok(taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList()));
  }

  private TaskDto convertToDto(Task task) {
    return mapper.map(task, TaskDto.class);
  }

  private Task convertToCreateEntity(Dto dto) {
    return mapper.map(dto, Task.class);
  }

}
