package ua.com.todo;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.todo.model.Status;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@AllArgsConstructor
class ApplicationTests {

  private final TaskService taskService;

  @Test
  void getList() {
    List<Task> list = taskService.getAll();
    assertEquals(5, list.size());
  }

  @Test
  void saveTask() {
    taskService.save(new Task("Write tests", "Write tests for the additional functionality", 9));
    List<Task> list = taskService.getAll();
    assertEquals(6, list.size());
    taskService.delete(5L);
  }

  @Test
  void deleteTask() {
    taskService.delete(2L);
    List<Task> list = taskService.getAll();
    assertEquals(4, list.size());
    taskService.save(new Task("Write tests", "Write tests for the additional functionality", 9));
  }

  @Test
  void getByName() {
    List<Task> tasks = taskService.getByName("res");
    assertEquals(2, tasks.size());
  }

  @Test
  void getById() {
    Task task = taskService.getById(1L);
    assertEquals("Rest", task.getName());
  }

  @Test
  void changeStatus() {
    taskService.changeStatus(1L, "running");
    Task task = taskService.getById(1L);
    assertEquals(Status.RUNNING, task.getStatus());
  }
}
