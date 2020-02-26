package ua.com.todo.service;

import org.springframework.stereotype.Service;
import ua.com.todo.model.Task;

import java.util.List;

@Service
public interface TaskService extends CommonService<Task, Long> {
  Task changeStatus(Long id, String status);

  List<Task> getByName(String name);

  List<Task> getStatistic(String date, int num);
}
