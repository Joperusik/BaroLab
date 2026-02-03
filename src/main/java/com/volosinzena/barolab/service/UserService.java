package com.volosinzena.barolab.service;


import com.volosinzena.barolab.service.model.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User getUserById(UUID userId);

    List<User> getAllUsers();

    User activateUser(UUID userId);

    User blockUser(UUID userId);

}
