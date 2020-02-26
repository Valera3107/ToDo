package ua.com.todo.service;

import org.springframework.stereotype.Service;
import ua.com.todo.model.Task;

import java.util.List;

/**
 * DAO for the {@link Task} objects.
 * Besides the basic CRUD methods it provides a methods to change status of the task, get list of Tasks by name
 * and get statistic per date and number of the date.
 *
 * @author Valera
 */
@Service
public interface TaskService extends CommonService<Task, Long> {

  /**
   * Change the status of the {@link Task} task.
   * @param status Task status
   * @param id Task id
   * @return loaded Task or return null
   */
  Task changeStatus(Long id, String status);

  /**
   * Get the list of the {@link Task} tasks.
   * @param name Task name
   * @return list of the tasks or null
   */
  List<Task> getByName(String name);

  /**
   * Get the list of the {@link Task} tasks.
   * @param date string view one of {@link ua.com.todo.model.StatisticDate} enum constant
   * @param num number of the date
   * @return list of the tasks or null
   */
  List<Task> getStatistic(String date, int num);
}
