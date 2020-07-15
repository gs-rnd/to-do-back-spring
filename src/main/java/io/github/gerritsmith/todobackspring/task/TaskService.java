package io.github.gerritsmith.todobackspring.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(long id) {
        return taskRepository.findById(id);
    }

    @Transactional
    public Task addTask(Task task) {
        removeWhiteSpace(task);
        return taskRepository.save(task);
    }

    @Transactional
    public Task updateTask(Task task) {
        removeWhiteSpace(task);
        return taskRepository.save(task);
    }

    private void removeWhiteSpace(Task task) {
        task.setTitle(task.getTitle().trim());
        task.setDescription(task.getDescription().trim());
    }

}
