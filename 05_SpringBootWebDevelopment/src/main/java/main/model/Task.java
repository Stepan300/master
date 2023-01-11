package main.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Task
{
      @Id
      @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String taskName;
    private String status;
    private String priority;
      @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
      @Temporal(TemporalType.DATE)
      @DateTimeFormat(pattern = "dd-MM-yyyy")
    private Date dueDate;
      @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
      @JoinColumn(name = "executor_id")
    private Executor executor;
    // ==================================================

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getTaskName() {return taskName;}
    public void setTaskName(String taskName) {this.taskName = taskName;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public void setStatus() {this.status = StatusTaskType.getRandomStatusType();}


    public String getPriority() {return priority;}
    public void setPriority(String priority) {this.priority = priority;}
    public void setPriority() {this.priority = PriorityTaskType.getRandomPriorityType();}

    public Date getDueDate() {return dueDate;}
    public void setDueDate(Date dueDate) {this.dueDate = dueDate;}

    public Executor getExecutor() {return executor;}
    public void setExecutor(Executor executor) {this.executor = executor;}
    public void setRandomExecutorName() {
        executor.setName(executor.getRandomName());}
    public void setRandomExecutorPosition() {
        executor.setPosition(executor.getRandomPosition());}
}