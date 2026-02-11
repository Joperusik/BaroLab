package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.UserAlreadyExistsException;
import com.volosinzena.barolab.exception.UserNotFoundException;
import com.volosinzena.barolab.service.model.Role;
import com.volosinzena.barolab.service.model.Status;
import com.volosinzena.barolab.service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final HashMap<UUID, User> userHashMap = new HashMap<>();

    @Override
    public List<User> getAllUsers() {
        return userHashMap.values().stream().toList();
    }

    @Override
    public User signUp(String login, String email, String password) {



        log.info("SignUp Request");
        Optional<User> optionalUser = userHashMap.values().stream()
                .filter(user -> user.getLogin().equals(login)).findFirst();

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(login);
        }

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setPassword(password);

        Instant now = Instant.now();

        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        user.setStatus(Status.ACTIVE);
        user.setRole(Role.USER);

        userHashMap.put(user.getId(), user);

        log.info("Succsessfully created");

        return user;
    }

    @Override
    public User getUserById(UUID userId) {
        User user = userHashMap.get(userId);

        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException(userId);
        }

    }

    @Override
    public User activateUser(UUID userId) {
        User user = userHashMap.get(userId);

        user.setStatus(Status.ACTIVE);
        user.setUpdatedAt(Instant.now());
        userHashMap.put(user.getId(), user);

        return user;
    }

    @Override
    public User blockUser(UUID userId) {
        User user = userHashMap.get(userId);

        user.setStatus(Status.BLOCKED);
        user.setUpdatedAt(Instant.now());
        userHashMap.put(user.getId(), user);

        return user;
    }
}
