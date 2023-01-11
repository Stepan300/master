package main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class TaskExecutor {

      @JsonProperty("id")
    private int taskIdDTO;
      @JsonProperty("task")
    private String taskName;
      @JsonFormat(pattern = "dd-MM-yyyy")
      @JsonProperty("due date")
    private Date dueDate;
      @JsonProperty("executor name")
    private String executorName;

    public int getTaskIdDTO() {
        return taskIdDTO;
    }
    public void setTaskIdDTO(int taskIdDTO) {
        this.taskIdDTO = taskIdDTO;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Date getDueDate() {
        return dueDate;
    }
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getExecutorName() {
        return executorName;
    }
    public void setExecutorName(String executorName) {
        this.executorName = executorName;
    }

    public String toString() {
        return  "\n\tid : " + taskIdDTO + "\n" +
                "\ttask : " + taskName + "\n" +
                "\tdue Date : " + dueDate + "\n" +
                "\texecutor name : " + executorName;
    }
}
