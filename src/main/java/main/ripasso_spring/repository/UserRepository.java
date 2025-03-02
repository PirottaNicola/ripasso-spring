package main.ripasso_spring.repository;

import main.ripasso_spring.model.entity.User;
import main.ripasso_spring.model.entity.UserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, UserId> {
  Optional<User> findByEmail(String email);
}
