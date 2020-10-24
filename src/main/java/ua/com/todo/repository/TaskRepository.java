package ua.com.todo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.com.todo.model.Task;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  List<Task> findByNameContainingIgnoreCase(String name);

  List<Task> findByFinishTaskBetween(LocalDateTime from, LocalDateTime to);

  List<Task> findByFinishTaskGreaterThanEqual(LocalDateTime from);

  List<Task> findByFinishTaskLessThanEqual(LocalDateTime to);

  List<Task> findByFinishTaskIsNotNull();

}
