package ru.netology.backend.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.netology.backend.exception.BadRequest;
import ru.netology.backend.model.Roles;
import ru.netology.backend.model.Users;
import ru.netology.backend.repository.RolesRepository;
import ru.netology.backend.repository.UserRepository;

import java.util.Optional;


@Component
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;

    @Override
    public JwtUser loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Users> user = userRepository.findByLogin(login);
        Roles role = rolesRepository.getRole(login);
        if (user.get() == null) {
            throw new BadRequest("Bad credentials");
        }
        JwtUser jwtUser = new JwtUser(user.get().getId(), user.get().getLogin(), user.get().getPassword(), role);
        return jwtUser;
    }
}
