package com.ipsos.entities.dtos;


import com.ipsos.entities.Project;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.util.ArrayList;
import java.util.List;

import static com.ipsos.constants.ErrorMessages.AuthOperations.*;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

    @NotNull(message = USERNAME_REQUIRED)
    @Size(min = 2, max = 20, message = USERNAME_LENGTH_ERROR)
    private String username;

    @NotNull(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 20, message = PASSWORD_LENGTH_ERROR)
    private String password;

    private List<Project> projects;

    public UserDto() {
        this.projects = new ArrayList<>();
    }

    public UserDto(String username, String password) {
        this();
        this.setUsername(username);
        this.setPassword(password);
    }

}
