package main.service;

import main.dto.TaskExecutor;
import main.model.Task;
import main.repository.ToDoRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoService {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<TaskExecutor> getAllTaskExecutor() {
        Iterable<Task> taskIterable = toDoRepository.findAll();
        List<TaskExecutor> taskExecutors = new ArrayList<>();
        for (Task task : taskIterable) {
            taskExecutors.add(convertToTaskExecutor(task));
        }
        return taskExecutors;
    }

    public TaskExecutor getTaskExecutor(int id) {
        Task task = toDoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The element with id № " + id +
                        "is not found"));
        return convertToTaskExecutor(task);
    }

    private TaskExecutor convertToTaskExecutor(Task task) {
        return modelMapper.map(task, TaskExecutor.class);
    }
}
