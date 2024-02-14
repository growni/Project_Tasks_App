package com.ipsos.entities.dtos;

import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

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

    public ProjectDto(String name) {
        this.status = Status.NOT_STARTED;
        this.priority = Priority.LOW;
        this.name = name;
        this.dueDate = LocalDate.now();
    }
}
