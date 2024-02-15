package com.ipsos.entities.dtos;


import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class TaskDto {

    private String description;
    private LocalDate due_date;
    private Priority priority;
    private Status status;
    private Long projectId;

    public TaskDto() {
        this.due_date = LocalDate.now();
        this.priority = Priority.LOW;
        this.status = Status.NOT_STARTED;
    }

    public TaskDto(String description) {
        this();
        this.description = description;
    }
}
