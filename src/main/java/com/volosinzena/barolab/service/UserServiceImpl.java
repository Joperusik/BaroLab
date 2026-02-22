package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.UserAlreadyExistsException;
import com.volosinzena.barolab.exception.UserNotFoundException;
import com.volosinzena.barolab.mapper.UserMapper;
import com.volosinzena.barolab.repository.UserRepository;
import com.volosinzena.barolab.service.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDomain)
                .toList();
    }

    @Override
    public User signUp(String login, String email, String username, String password) {

        log.info("Sign up request login={}", login);
        Optional<com.volosinzena.barolab.repository.entity.UserEntity> optionalUser = userRepository.findByLogin(login);

        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException(login);
        }

        com.volosinzena.barolab.repository.entity.UserEntity entity = new com.volosinzena.barolab.repository.entity.UserEntity();
        entity.setLogin(login);
        entity.setEmail(email);
        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));

        Instant now = Instant.now();

        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setRole(com.volosinzena.barolab.repository.entity.Role.USER);

        com.volosinzena.barolab.repository.entity.UserEntity savedEntity = userRepository.save(entity);

        log.info("Sign up success login={} userId={}", login, savedEntity.getId());

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public User login(String login, String password) {
        com.volosinzena.barolab.repository.entity.UserEntity entity = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(login)); // Assuming constructor accepting String exists,
        // otherwise modify

        if (!passwordEncoder.matches(password, entity.getPassword())) {
            log.warn("Login failed bad credentials login={}", login);
            throw new com.volosinzena.barolab.exception.BadCredentialsException("Invalid password");
        }

        if (entity.getStatus() == com.volosinzena.barolab.repository.entity.Status.BLOCKED) {
            log.warn("Login blocked login={}", login);
            throw new com.volosinzena.barolab.exception.UserBlockedException(login);
        }

        log.info("Login success login={} userId={}", login, entity.getId());
        return userMapper.toDomain(entity);
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<com.volosinzena.barolab.repository.entity.UserEntity> optionalEntity = userRepository.findById(userId);

        if (optionalEntity.isPresent()) {
            return userMapper.toDomain(optionalEntity.get());
        } else {
            throw new UserNotFoundException(userId);
        }

    }

    @Override
    public User activateUser(UUID userId) {
        Optional<com.volosinzena.barolab.repository.entity.UserEntity> optionalEntity = userRepository.findById(userId);

        if (optionalEntity.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        com.volosinzena.barolab.repository.entity.UserEntity entity = optionalEntity.get();

        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public User blockUser(UUID userId) {
        Optional<com.volosinzena.barolab.repository.entity.UserEntity> optionalEntity = userRepository.findById(userId);

        if (optionalEntity.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        com.volosinzena.barolab.repository.entity.UserEntity entity = optionalEntity.get();

        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

    @Override
    public User updateRole(UUID userId, com.volosinzena.barolab.controller.dto.Role role) {
        Optional<com.volosinzena.barolab.repository.entity.UserEntity> optionalEntity = userRepository.findById(userId);

        if (optionalEntity.isEmpty()) {
            throw new UserNotFoundException(userId);
        }

        com.volosinzena.barolab.repository.entity.UserEntity entity = optionalEntity.get();

        entity.setRole(com.volosinzena.barolab.repository.entity.Role.valueOf(role.name()));
        entity.setUpdatedAt(Instant.now());
        com.volosinzena.barolab.repository.entity.UserEntity savedEntity = userRepository.save(entity);

        return userMapper.toDomain(savedEntity);
    }

}
