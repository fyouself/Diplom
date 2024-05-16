package ru.netology.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.netology.backend.dto.UserDto;
import ru.netology.backend.servise.UserServise;

import java.util.Map;

@RestController
public class UserController {
    private UserServise userServise;

    public UserController(UserServise userServise) {
        this.userServise = userServise;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> userAutorization(@RequestBody() UserDto body, HttpServletRequest request) {
        return userServise.findByLogin(body, request);
    }
}




