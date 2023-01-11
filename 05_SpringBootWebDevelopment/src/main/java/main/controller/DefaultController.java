package main.controller;

import main.dto.TaskExecutor;
import main.model.Task;
import main.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class DefaultController {

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private ToDoController toDoController;

    @Value("${forExample.value}")
    private String forExample;

    @RequestMapping("/")
    public String index(Model model) {
        List<TaskExecutor> taskExecutors = toDoService.getAllTaskExecutor();
        model.addAttribute("ToDoList", taskExecutors);
        model.addAttribute("taskCount", taskExecutors.size());
        model.addAttribute("forExample", forExample);
        return "index";
    }

//    @RequestMapping("/addNewTask")
//    public String addNewTask(Model model) {
//        Task task = new Task();
//        model.addAttribute("task", task);
//        return "new_task";
//    }
}
