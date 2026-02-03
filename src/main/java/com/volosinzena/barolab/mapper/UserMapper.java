package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.UserDto;
import com.volosinzena.barolab.service.model.Role;
import com.volosinzena.barolab.service.model.Status;
import com.volosinzena.barolab.service.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toDomain(UserDto dto){
        User user = new User();

        user.setId(dto.getId());
        user.setLogin(dto.getLogin());
        user.setEmail(dto.getEmail());
        user.setCreatedAt(dto.getCreatedAt());
        user.setUpdatedAt(dto.getUpdatedAt());
        user.setStatus(String.valueOf(Status.valueOf(dto.getStatus())));
        user.setRole(String.valueOf(Role.valueOf(dto.getRole())));

        return user;
    }

    public UserDto toDto(User user){
        UserDto dto = new UserDto();

        dto.setId(user.getId());
        dto.setLogin(user.getLogin());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setStatus(String.valueOf(com.volosinzena.barolab.controller.dto.Status.valueOf(user.getStatus())));
        dto.setRole(String.valueOf(com.volosinzena.barolab.controller.dto.Role.valueOf(user.getRole())));

        return dto;
    }
}
