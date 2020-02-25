package ua.com.todo.service;

import org.springframework.stereotype.Repository;
import ua.com.todo.model.Task;

import java.util.List;

@Repository
public interface TaskService extends CommonService<Task, Long> {
  void changeStatus(Long id, String status);

  List<Task> getByName(String name);

  List<Task> getStatistic(String date, String num);
}
