package ua.com.todo.todo;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import ua.com.todo.todo.model.Status;
import ua.com.todo.todo.model.Task;
import ua.com.todo.todo.service.TaskService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("/application-test.properties")
class ApplicationTests {

  private final TaskService taskService;

  @Autowired
  ApplicationTests(TaskService taskService) {
    this.taskService = taskService;
  }

  @Test
	void getList() {
    List<Task> list = taskService.getAll();
    assertEquals(5, list.size());
	}

	@Test
  void saveTask(){
    taskService.save(new Task("Write tests", "Write tests for the additional functionality", 9, Status.FINISHED));
    List<Task> list = taskService.getAll();
    assertEquals(6, list.size());
    taskService.delete("5");
  }

  @Test
  void deleteTask(){
    taskService.delete("2");
    List<Task> list = taskService.getAll();
    assertEquals(4, list.size());
    taskService.save(new Task("Write tests", "Write tests for the additional functionality", 9, Status.FINISHED));
  }

  @Test
  void getByName(){
    List<Task> tasks = taskService.getByName("res");
    assertEquals(2, tasks.size());
  }

  @Test
  void getById(){
    Task task = taskService.getById("1");
    assertEquals("Rest", task.getName());
  }

  @Test
  void changeStatus(){
    taskService.changeStatus("1", "running");
    Task task = taskService.getById("1");
    assertEquals(Status.RUNNING, task.getStatus());
  }
}
