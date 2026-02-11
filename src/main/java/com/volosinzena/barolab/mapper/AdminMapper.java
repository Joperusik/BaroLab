package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.AdminDto;
import com.volosinzena.barolab.repository.entity.AdminEntity;
import com.volosinzena.barolab.service.model.Admin;
import com.volosinzena.barolab.service.model.Role;
import com.volosinzena.barolab.service.model.Status;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public Admin toDomain(AdminDto dto) {
        Admin admin = new Admin();

        admin.setId(dto.getId());
        admin.setLogin(dto.getLogin());
        admin.setCreatedAt(dto.getCreatedAt());
        admin.setUpdatedAt(dto.getUpdatedAt());
        if (dto.getStatus() != null) {
            admin.setStatus((Status.valueOf(dto.getStatus().name())));
        }
        if (dto.getRole() != null) {
            admin.setRole((Role.valueOf(dto.getRole().name())));
        }

        return admin;
    }

    public AdminDto toDto(Admin admin) {
        AdminDto dto = new AdminDto();

        dto.setId(admin.getId());
        dto.setLogin(admin.getLogin());
        dto.setCreatedAt(admin.getCreatedAt());
        dto.setUpdatedAt(admin.getUpdatedAt());
        if (admin.getStatus() != null) {
            dto.setStatus(com.volosinzena.barolab.controller.dto.Status.valueOf(admin.getStatus().name()));
        }
        if (admin.getRole() != null) {
            dto.setRole(com.volosinzena.barolab.controller.dto.Role.valueOf(admin.getRole().name()));
        }

        return dto;
    }


    public Admin toDomain(AdminEntity entity) {

        Admin admin = new Admin();
        admin.setId(entity.getId());
        admin.setLogin(entity.getLogin());
        admin.setRole(Role.valueOf(entity.getRole().name()));
        admin.setCreatedAt(entity.getCreatedAt());
        admin.setUpdatedAt(entity.getUpdatedAt());
        admin.setStatus(Status.valueOf(entity.getStatus().name()));

        return admin;
    }

    public AdminEntity toEntity(Admin admin) {

        AdminEntity entity = new AdminEntity();
        entity.setId(admin.getId());
        entity.setLogin(admin.getLogin());
        entity.setRole(com.volosinzena.barolab.repository.entity.Role.valueOf(entity.getRole().name()));
        entity.setCreatedAt(admin.getCreatedAt());
        entity.setUpdatedAt(admin.getUpdatedAt());
        entity.setStatus(com.volosinzena.barolab.repository.entity.Status.valueOf(admin.getStatus().name()));

        return entity;
    }

}
