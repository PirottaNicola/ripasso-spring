package main.ripasso_spring.service;

import main.ripasso_spring.exception.UserAlreadyExistsException;
import main.ripasso_spring.model.entity.User;
import main.ripasso_spring.model.entity.UserId;
import main.ripasso_spring.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) throws UserAlreadyExistsException {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findById(new UserId(user.getName(), user.getEmail()));
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "User with name '" + user.getName() + "' and email '" + user.getEmail() + "' already exists");
        }
        return userRepository.save(user);
    }
}
