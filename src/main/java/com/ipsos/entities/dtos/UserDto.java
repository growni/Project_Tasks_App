package com.ipsos.entities.dtos;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import static com.ipsos.constants.Messages.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull(message = USERNAME_REQUIRED)
    @Size(min = 2, max = 20, message = USERNAME_LENGTH_ERROR)
    private String username;

    @NotNull(message = PASSWORD_REQUIRED)
    @Size(min = 6, max = 20, message = PASSWORD_LENGTH_ERROR)
    private String password;

}
