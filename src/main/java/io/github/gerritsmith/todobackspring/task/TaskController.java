package io.github.gerritsmith.todobackspring.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/tasks/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping("/tasks")
    public Task addNewTask(@RequestBody @Valid Task task) {
        return taskService.addTask(task);
    }

    @PutMapping("/tasks/{id}")
    public Task updateTask(@PathVariable Long id,
                           @RequestBody @Valid Task task) {
        if (task.getId().equals(id)) {
            return taskService.updateTask(task);
        }
        return null;
    }

    @DeleteMapping("/tasks/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

}
