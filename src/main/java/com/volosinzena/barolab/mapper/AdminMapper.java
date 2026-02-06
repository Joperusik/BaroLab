package com.volosinzena.barolab.mapper;

import com.volosinzena.barolab.controller.dto.AdminDto;
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
            admin.setStatus(String.valueOf(Status.valueOf(dto.getStatus())));
        }
        if (dto.getRole() != null) {
            admin.setRole(String.valueOf(Role.valueOf(dto.getRole())));
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
            dto.setStatus(String.valueOf(com.volosinzena.barolab.controller.dto.Status.valueOf(admin.getStatus())));
        }
        if (admin.getRole() != null) {
            dto.setRole(String.valueOf(com.volosinzena.barolab.controller.dto.Role.valueOf(admin.getRole())));
        }

        return dto;
    }
}
