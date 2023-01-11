package main.controller;

import main.dto.TaskExecutor;
import main.repository.ToDoRepository;
import main.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.model.Task;

import java.util.List;
import java.util.Optional;

@RestController
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ToDoService toDoService;

    @PostMapping("/task/")
    public int add(@RequestBody Task task) {
        if (task.getStatus() == null) {
            task.setStatus();
        }
        if (task.getPriority() == null) {
            task.setPriority();
        }
        if (task.getExecutor().getName() == null) {
            task.setRandomExecutorName();
        }
        if (task.getExecutor().getPosition() == null) {
            task.setRandomExecutorPosition();
        }
        return toDoRepository.save(task).getId();
    }

    @GetMapping("/task/")
    public List<TaskExecutor> getList() {
         return toDoService.getAllTaskExecutor();
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<TaskExecutor> get(@PathVariable(value = "id") int id) {
        Optional<Task> optionalTask = toDoRepository.findById(id);
        return optionalTask.map(task -> new ResponseEntity<>(toDoService.getTaskExecutor(id),
            HttpStatus.OK)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/task/{id}")
    public boolean update(@PathVariable(value = "id") int id, @RequestBody Task task) {
        if (toDoRepository.existsById(id)) {
            task.setId(id);
            task.getExecutor().setExecutorId(++id);
            if (task.getStatus() == null) {
                task.setStatus();
            }
            if (task.getPriority() == null) {
                task.setPriority();
            }
            if (task.getExecutor().getName() == null) {
                task.setRandomExecutorName();
            }
            if (task.getExecutor().getPosition() == null) {
                task.setRandomExecutorPosition();
            }
            toDoRepository.save(task);
            return true;
        } else {
            System.out.println("Attention!!! The element with index № " + id + " is not exist");
            return false;
        }
    }

    @DeleteMapping("/task/")
    public void deleteList() {
        toDoRepository.deleteAll();
    }

    @DeleteMapping("/task/{id}")
    public boolean delete(@PathVariable(value = "id") int id) {
        if (toDoRepository.existsById(id)) {
            toDoRepository.deleteById(id);
            return true;
        }
        System.out.println("Attention!!! The element with index № " + id + " is absent");
        return false;
    }
}