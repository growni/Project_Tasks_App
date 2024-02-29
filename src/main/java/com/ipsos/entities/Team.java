package com.ipsos.entities;

import com.ipsos.config.StringListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "teams")
public class Team extends BaseEntity{

    @Column(nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "leader_id")
    private User teamLeader;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> members;

    @Column()
    @Convert(converter = StringListConverter.class)
    private List<String> joinRequestUsernames;

    public Team() {
        this.members = new ArrayList<>();
        this.joinRequestUsernames = new ArrayList<>();
    }

    public Team(String name) {
        this();
        this.name = name;
    }
}
