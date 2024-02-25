package com.ipsos.entities.dtos;


import com.ipsos.entities.Project;
import com.ipsos.entities.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ipsos.constants.ErrorMessages.AuthOperations.*;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotNull(message = USERNAME_REQUIRED)
    @Size(min = 2, max = 20, message = USERNAME_LENGTH_ERROR)
    private String username;

    @NotNull(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 20, message = PASSWORD_LENGTH_ERROR)
    private String password;

    @NotNull(message = CONFIRM_PASSWORD_REQUIRED)
    private String confirmPassword;

    private Set<Role> roles;

    private List<Project> projects;

    private boolean isEnabled;

    public UserDto() {
        this.projects = new ArrayList<>();
        this.roles = new HashSet<>();
    }

    public UserDto(String username, String password, String confirmPassword) {
        this();
        this.setUsername(username);
        this.setPassword(password);
        this.setConfirmPassword(confirmPassword);
    }

    public UserDto(String username, String password) {
        this();
        this.setUsername(username);
        this.setPassword(password);
    }

}
