package ua.com.todo.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.com.todo.dto.Dto;
import ua.com.todo.dto.TaskDto;
import ua.com.todo.model.StatisticDate;
import ua.com.todo.model.Status;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;
import ua.com.todo.validation.interafaces.ValidTaskStatus;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
  private static final String FILENAME = "report.xlsx";

  private final TaskService taskServiceImpl;
  private final ModelMapper mapper;


  @GetMapping
  @ApiOperation(value = "Get all tasks", response = TaskDto.class, responseContainer = "List")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully get all tasks"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public List<TaskDto> list() {
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get task by id", response = TaskDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully get task by id"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public TaskDto getTaskById(@PathVariable Long id) {
    return convertToDto(taskServiceImpl.getById(id));
  }

  @GetMapping("/filter/{name}")
  @ApiOperation(value = "Get tasks by name", response = TaskDto.class, responseContainer = "List")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully get tasks by name"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public List<TaskDto> getTaskByName(@PathVariable("name") String name) {
    return taskServiceImpl.getByName(name).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ApiOperation(value = "Save task", response = TaskDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully saved task"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public List<TaskDto> save(@RequestBody @Valid Dto taskDto) {
    taskServiceImpl.save(convertToCreateEntity(taskDto));
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @GetMapping("/statistic/{date}/{num}")
  @ApiOperation(value = "Get statistic by tasks", response = TaskDto.class, responseContainer = "List")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully get statistic by tasks"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public List<TaskDto> getStatistic(@PathVariable StatisticDate date, @PathVariable Integer num) {
    return taskServiceImpl.getStatistic(date, num).stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Update task by id", response = TaskDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully updated task by id"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public TaskDto update(@PathVariable Long id, @RequestBody @Valid Dto taskDto) {
    Task task = convertToCreateEntity(taskDto);
    return convertToDto(taskServiceImpl.update(task, id));
  }

  @PutMapping("/{id}/{status}")
  @ApiOperation(value = "Get task by id and status", response = TaskDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully get task by id and status"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public TaskDto changeStatus(@PathVariable Long id, @ValidTaskStatus @PathVariable Status status) {
    return convertToDto(taskServiceImpl.changeStatus(id, status));
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete task by id", response = TaskDto.class)
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully deleted task by id"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
  })
  public List<TaskDto> delete(@PathVariable Long id) {
    taskServiceImpl.delete(id);
    return taskServiceImpl.getAll().stream().map(this::convertToDto).collect(Collectors.toList());
  }

  @PostMapping(path = "/download-task-report")
  @ApiOperation(value = "Download task report")
  @ApiResponses(value = {
          @ApiResponse(code = 200, message = "Successfully download task report"),
          @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
          @ApiResponse(code = 403, message = "Access to the resource you've tried to reach is forbidden"),
          @ApiResponse(code = 404, message = "The resource you've tried to reach was not found")
  })
  public ResponseEntity<Resource> downloadTaskReport(@RequestParam(required = false) LocalDateTime from,
                                                     @RequestParam(required = false) LocalDateTime to) {

    ByteArrayInputStream content = taskServiceImpl.loadTaskReport(from, to);
    InputStreamResource file = new InputStreamResource(content);
    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + FILENAME)
            .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
            .body(file);
  }

  private TaskDto convertToDto(Task task) {
    return mapper.map(task, TaskDto.class);
  }

  private Task convertToCreateEntity(Dto dto) {
    return mapper.map(dto, Task.class);
  }

}
