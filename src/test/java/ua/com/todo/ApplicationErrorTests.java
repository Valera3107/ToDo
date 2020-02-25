package ua.com.todo;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.todo.model.Task;
import ua.com.todo.service.TaskService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
@AllArgsConstructor
public class ApplicationErrorTests {

  private final TaskService taskService;

  @Test
  void errorGetById() {
    Task task = taskService.getById(4924L);
    assertEquals(new Task("No name", "No any task with such id", 0), task);
  }

  @Test
  void errorGetByName(){
    List<Task> list = taskService.getByName("Abra cadabra");
    assertEquals(0, list.size());
  }
}
