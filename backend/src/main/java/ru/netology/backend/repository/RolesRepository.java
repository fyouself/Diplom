package ru.netology.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.backend.model.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {
    @Query(value = "select r.id ,upper(r.role) as role  from users u join roles r " +
            "on u.role_id = r.id where u.login = ?1", nativeQuery = true)
    Roles getRole(String username);
}

