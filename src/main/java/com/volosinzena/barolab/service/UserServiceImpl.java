package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.Status;
import com.volosinzena.barolab.service.model.User;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final HashMap<UUID, User> userHashMap = new HashMap<>();


    @Override
    public User getUserById(UUID id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return userHashMap.values().stream().toList();
    }

    @Override
    public User getUseById(UUID userId) {
        return userHashMap.get(userId);
    }

    @Override
    public User activateUser(UUID userId) {
        User user = userHashMap.get(userId);

        user.setStatus(String.valueOf(Status.ACTIVE));
        user.setUpdatedAt(Instant.now());
        userHashMap.put(UUID.fromString(user.getId()), user);

        return user;
    }

    @Override
    public User blockUser(UUID userId) {
        User user = userHashMap.get(userId);

        user.setStatus(String.valueOf(Status.BLOCKED));
        user.setUpdatedAt(Instant.now());
        userHashMap.put(UUID.fromString(user.getId()), user);

        return user;
    }
}
