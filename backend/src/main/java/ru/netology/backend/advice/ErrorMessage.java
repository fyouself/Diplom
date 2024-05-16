package ru.netology.backend.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class ErrorMessage {
    private UUID id;
    private String description;
}
