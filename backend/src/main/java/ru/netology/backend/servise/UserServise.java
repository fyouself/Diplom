package ru.netology.backend.servise;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.netology.backend.dto.UserDto;
import ru.netology.backend.exception.BadRequest;
import ru.netology.backend.model.Roles;
import ru.netology.backend.model.Users;
import ru.netology.backend.repository.RolesRepository;
import ru.netology.backend.repository.UserRepository;
import ru.netology.backend.security.JwtUtilities;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServise {
    @Value("${bcrypt.salt}")
    private int salt;
    private final UserRepository repository;
    private final RolesRepository rolesRepository;
    private final JwtUtilities jwtUtilities;

    public UserServise(UserRepository repository, RolesRepository rolesRepository, JwtUtilities jwtUtilities) {
        this.repository = repository;
        this.rolesRepository = rolesRepository;
        this.jwtUtilities = jwtUtilities;    }

    public ResponseEntity<Map<Object, Object>> findByLogin(UserDto body, HttpServletRequest request) {
        Optional<Users> user = repository.findByLogin(body.getLogin());
        Roles role = rolesRepository.getRole(body.getLogin());
        if (user.isPresent()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(salt, new SecureRandom(new byte[20]));
            if (encoder.matches(body.getPassword(), user.get().getPassword())) {
                String token = jwtUtilities.generateToken(user.get().getLogin(), Collections.singletonList(role.toString()));
                HttpSession session = request.getSession();
                session.setAttribute("username", user.get().getLogin());
                session.setAttribute("user-id", user.get().getId());
                Map<Object, Object> response = new HashMap<>();
                response.put("email", user.get().getLogin());
                response.put("password", user.get().getPassword());
                response.put("auth-token", token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                throw new BadRequest("password");
            }
        } else {
            throw new BadRequest("email");
        }
    }
}
