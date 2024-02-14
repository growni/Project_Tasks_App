package com.ipsos.entities.dtos;


import com.ipsos.entities.Project;
import com.ipsos.exceptions.InvalidDataException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.ArrayList;
import java.util.List;

import static com.ipsos.constants.Errors.*;
import static com.ipsos.constants.Regex.PASSWORD_REGEX;
import static com.ipsos.constants.Regex.USERNAME_REGEX;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = USERNAME_REQUIRED)
    @Size(min = 2, max = 20, message = USERNAME_LENGTH_ERROR)
    private String username;

    @NotNull(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 20, message = PASSWORD_LENGTH_ERROR)
    private String password;

    private List<Project> projects;

    public UserDto(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
        this.projects = new ArrayList<>();
    }

}
