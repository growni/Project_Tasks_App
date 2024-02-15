package com.ipsos.entities.dtos;

import com.ipsos.entities.Task;
import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {
    @Size(max = 50)
    private String name;
    @Size(max = 15)
    private String jobNumber;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private List<Task> tasks;

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
}
