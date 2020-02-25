package ua.com.todo.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.com.todo.model.StatisticDate;
import ua.com.todo.model.Status;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;
import ua.com.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  public Task save(Task task) {
    task.setStartTask(LocalDateTime.now());
    task.setStatus(Status.NEW);
    return taskRepository.save(task);
  }

  public List<Task> getAll() {
    return taskRepository.findAll();
  }

  public List<Task> getByName(String name) {
    return taskRepository.findByNameContainingIgnoreCase(name);
  }

  public Task getById(Long id) {
    Optional<Task> taskFromDB = taskRepository.findById(id);
    return taskFromDB.orElseThrow(() -> new IllegalArgumentException("Task with such id doesn't exist!"));
  }

  public List<Task> update(Task task, Long taskId) {
    Task taskFromDB = getById(taskId);
    taskFromDB.setName(task.getName());
    taskFromDB.setDescription(task.getDescription());
    taskRepository.save(taskFromDB);
    return getAll();
  }

  public void delete(Long id) {
    Task task = getById(id);
    taskRepository.delete(task);
  }

  public void changeStatus(Long id, String status) {
    Status newStatus = Status.valueOf(status.toUpperCase());
    Task task = getById(id);
    task.setStatus(newStatus);
    if (Status.FINISHED.equals(task.getStatus())) {
      task.setFinishTask(LocalDateTime.now());
    }
    taskRepository.save(task);
  }

  public List<Task> getStatistic(String date, String num) {
    StatisticDate statisticDate = StatisticDate.valueOf(date.toUpperCase());
    int numOfDate = Integer.parseInt(num);
    switch (statisticDate) {
      case DAY:
        LocalDateTime minusDays = LocalDateTime.now().minusDays(numOfDate);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusDays.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case WEEK:
        LocalDateTime minusWeeks = LocalDateTime.now().minusWeeks(numOfDate);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusWeeks.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case MONTH:
        LocalDateTime minusMonths = LocalDateTime.now().minusMonths(numOfDate);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusMonths.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      case YEAR:
        LocalDateTime minusYears = LocalDateTime.now().minusYears(numOfDate);
        return taskRepository.findAll().stream().filter(e -> e.getFinishTask() != null).filter(e -> minusYears.compareTo(e.getFinishTask()) < 0).collect(Collectors.toList());
      default:
        return Collections.emptyList();
    }
  }
}
