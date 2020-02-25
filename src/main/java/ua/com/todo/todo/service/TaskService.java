package ua.com.todo.todo.service;

import org.springframework.stereotype.Service;
import ua.com.todo.todo.model.StatisticDate;
import ua.com.todo.todo.model.Status;
import ua.com.todo.todo.model.Task;
import ua.com.todo.todo.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  public void save(Task task) {
    task.setStartTask(LocalDateTime.now());
    taskRepository.save(task);
  }

  public List<Task> getAll() {
    return taskRepository.findAll();
  }

  public List<Task> getByName(String name) {
    return taskRepository.findByNameContainingIgnoreCase(name);
  }

  public Task getById(String id) {
    Long taskId = Long.valueOf(id);
    Optional<Task> taskFromDB = taskRepository.findById(taskId);
    return taskFromDB.orElseGet(() -> new Task("No name", "No any task with such id", 0, Status.FINISHED));
  }

  public void update(Task task) {
    taskRepository.save(task);
  }

  public void delete(String id) {
    Task task = getById(id);
    taskRepository.delete(task);
  }

  public void changeStatus(String id, String status) {
    Status newStatus = Status.valueOf(status.toUpperCase());
    Task task = getById(id);
    task.setStatus(newStatus);
    if (Status.FINISHED.equals(task.getStatus()))
      task.setFinishTask(LocalDateTime.now());
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
