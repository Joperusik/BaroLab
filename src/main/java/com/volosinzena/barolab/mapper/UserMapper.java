package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.UserDto;
import com.volosinzena.barolab.service.model.Role;
import com.volosinzena.barolab.service.model.Status;
import com.volosinzena.barolab.service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserDto dto) {
        User user = new User();

        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        user.setStatus(Status.valueOf(String.valueOf(Status.valueOf(String.valueOf(dto.getStatus())))));
        user.setRole(Role.valueOf(String.valueOf(Role.valueOf(String.valueOf(dto.getRole())))));

        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setStatus(com.volosinzena.barolab.controller.dto.Status
                .valueOf(String.valueOf(Status.valueOf(user.getStatus().name()))));
        dto.setRole(com.volosinzena.barolab.controller.dto.Role.valueOf(user.getRole().name()));

        return dto;
    }

    public User toDomain(com.volosinzena.barolab.repository.entity.UserEntity entity) {
        if (entity == null) {
            return null;
        }
        User user = new User();
        user.setId(entity.getId());
        user.setLogin(entity.getLogin());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setStatus(Status.valueOf(entity.getStatus().name()));
        user.setRole(Role.valueOf(entity.getRole().name()));
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());
        return user;
    }

    public com.volosinzena.barolab.repository.entity.UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        com.volosinzena.barolab.repository.entity.UserEntity entity = new com.volosinzena.barolab.repository.entity.UserEntity();
        entity.setId(user.getId());
        entity.setLogin(user.getLogin());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        if (user.getStatus() != null) {
            entity.setStatus(com.volosinzena.barolab.repository.entity.Status.valueOf(user.getStatus().name()));
        }
        if (user.getRole() != null) {
            entity.setRole(com.volosinzena.barolab.repository.entity.Role.valueOf(user.getRole().name()));
        }
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }
}
