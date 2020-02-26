package ua.com.todo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.todo.model.StatisticDate;
import ua.com.todo.model.Status;
import ua.com.todo.model.Task;
import ua.com.todo.repository.TaskRepository;
import ua.com.todo.service.TaskService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  @Override
  public Task save(Task task) {
    task.setStartTask(LocalDateTime.now());
    task.setStatus(Status.NEW);
    return taskRepository.save(task);
  }

  @Override
  public List<Task> getAll() {
    return taskRepository.findAll();
  }

  @Override
  public List<Task> getByName(String name) {
    return taskRepository.findByNameContainingIgnoreCase(name);
  }

  @Override
  public Task getById(Long id) {
    Optional<Task> taskFromDB = taskRepository.findById(id);
    return taskFromDB.orElseThrow(() -> new IllegalArgumentException("Task with such id doesn't exist!"));
  }

  @Override
  public Task update(Task task, Long taskId) {
    Task taskFromDB = getById(taskId);
    taskFromDB.setName(task.getName());
    taskFromDB.setDescription(task.getDescription());
    taskRepository.save(taskFromDB);
    return taskFromDB;
  }

  @Override
  public void delete(Long id) {
    Task task = getById(id);
    taskRepository.delete(task);
  }

  @Override
  public Task changeStatus(Long id, String status) {
    Status newStatus = Status.valueOf(status.toUpperCase());
    Task task = getById(id);
    if (!task.getStatus().equals(Status.FINISHED)) {
      task.setStatus(newStatus);
      if (Status.FINISHED.equals(task.getStatus())) {
        task.setFinishTask(LocalDateTime.now());
      }
      taskRepository.save(task);
    }
    return task;
  }

  @Override
  public List<Task> getStatistic(String date, int num) {
    StatisticDate statisticDate = StatisticDate.valueOf(date.toUpperCase());
    switch (statisticDate) {
      case DAY:
        LocalDateTime minusDays = LocalDateTime.now().minusDays(num);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusDays.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case WEEK:
        LocalDateTime minusWeeks = LocalDateTime.now().minusWeeks(num);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusWeeks.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case MONTH:
        LocalDateTime minusMonths = LocalDateTime.now().minusMonths(num);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusMonths.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case YEAR:
        LocalDateTime minusYears = LocalDateTime.now().minusYears(num);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusYears.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      default:
        return Collections.emptyList();
    }
  }
}
