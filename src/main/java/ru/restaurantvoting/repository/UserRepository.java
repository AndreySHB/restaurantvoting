package ru.restaurantvoting.repository;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurantvoting.model.User;
import ru.restaurantvoting.util.validation.ValidationUtil;

import java.util.Optional;

@Tag(name = "User Controller")
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = LOWER(:email)")
    Optional<User> findByEmailIgnoreCase(String email);

    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int simpleDelete(int id);

     default void delete(int id) {
        ValidationUtil.checkModification(simpleDelete(id), id);
    }
}