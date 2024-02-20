package com.ipsos.entities.dtos;


import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TaskDto {
    private Long id;
    private String description;
    private Priority priority;
    private Status status;
    private Long projectId;

    public TaskDto() {
        this.priority = Priority.LOW;
        this.status = Status.NOT_STARTED;
    }

    public TaskDto(String description) {
        this();
        this.description = description;
    }

}
