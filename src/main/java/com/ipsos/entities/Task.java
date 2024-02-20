package com.ipsos.entities;

import com.ipsos.entities.enums.Priority;
import com.ipsos.entities.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "tasks")
public class Task extends BaseEntity{

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

}