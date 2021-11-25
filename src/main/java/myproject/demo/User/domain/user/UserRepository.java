package myproject.demo.User.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(Username username);

    Optional<User> findByUsername(Username username);
}
