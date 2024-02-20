package com.ipsos.entities.dtos;

import com.ipsos.entities.Task;
import com.ipsos.entities.User;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ProjectDto {
    private Long id;
    private String name;
    private String jobNumber;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private List<Task> tasks;
    private User user;

    public ProjectDto() {
        this.status = Status.NOT_STARTED;
        this.priority = Priority.LOW;
        this.dueDate = LocalDate.now();
        this.tasks = new ArrayList<>();
    }

    public ProjectDto(String name) {
        this();
        this.name = name;
    }

    public ProjectDto(String name, String jobNumber) {
        this();
        this.setName(name);
        this.setJobNumber(jobNumber);
    }
    public ProjectDto(String name, String jobNumber, String status) {
        this();
        this.setName(name);
        this.setJobNumber(jobNumber);
        this.setStatus(Status.valueOf(status));
    }
    public ProjectDto(String name, String jobNumber, String status, String priority) {
        this();
        this.setName(name);
        this.setJobNumber(jobNumber);
        this.setStatus(Status.valueOf(status));
        this.setPriority(Priority.valueOf(priority));
    }

    public ProjectDto(String name, String jobNumber, String status, String priority, LocalDate dueDate) {
        this.setName(name);
        this.setJobNumber(jobNumber);
        this.setStatus(Status.valueOf(status));
        this.setPriority(Priority.valueOf(priority));
        this.setDueDate(dueDate);
    }

    @Override
    public String toString() {
        return "ProjectDto{" +
                "id=" + id +
                ",\n name='" + name + '\'' +
                ",\n jobNumber='" + jobNumber + '\'' +
                ",\n status=" + status +
                ",\n priority=" + priority +
                ",\n dueDate=" + dueDate +
                ",\n tasks=" + tasks +
                ",\n user=" + user +
                '}';
    }
}
