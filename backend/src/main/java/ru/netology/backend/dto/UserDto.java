package ru.netology.backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Data
@AllArgsConstructor
@Getter
@ToString
public class UserDto {
    @NotEmpty
    private String login;
    @NotEmpty
    private String password;
}
