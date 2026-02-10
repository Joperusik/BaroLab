package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.AdminAlreadyExistsException;
import com.volosinzena.barolab.exception.AdminNotFoundException;
import com.volosinzena.barolab.service.model.Admin;
import com.volosinzena.barolab.service.model.Role;
import com.volosinzena.barolab.service.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private final HashMap<UUID, Admin> adminHashMap = new HashMap<>();

    @Override
    public List<Admin> getAllAdmins() {
        return adminHashMap.values().stream().toList();
    }

    @Override
    public Admin createAdmin(Admin admin) {
        log.info("Create Admin Request");
        Optional<Admin> optionalAdmin = adminHashMap.values().stream()
                .filter(a -> a.getLogin().equals(admin.getLogin())).findFirst();

        if (optionalAdmin.isPresent()) {
            throw new AdminAlreadyExistsException(admin.getLogin());
        }

        admin.setId(UUID.randomUUID());
        Instant now = Instant.now();
        admin.setCreatedAt(now);
        admin.setUpdatedAt(now);
        admin.setStatus(Status.ACTIVE);
        if (admin.getRole() == null) {
            admin.setRole(Role.ADMIN);
        }

        adminHashMap.put(admin.getId(), admin);

        log.info("Successfully created admin");

        return admin;
    }

    @Override
    public Admin getAdminById(UUID adminId) {
        Admin admin = adminHashMap.get(adminId);

        if (admin != null) {
            return admin;
        } else {
            throw new AdminNotFoundException(adminId);
        }
    }

    @Override
    public Admin activateAdmin(UUID adminId) {
        Admin admin = adminHashMap.get(adminId);
        if (admin == null) {
            throw new AdminNotFoundException(adminId);
        }

        admin.setStatus(Status.ACTIVE);
        admin.setUpdatedAt(Instant.now());
        adminHashMap.put(admin.getId(), admin);

        return admin;
    }

    @Override
    public Admin blockAdmin(UUID adminId) {
        Admin admin = adminHashMap.get(adminId);
        if (admin == null) {
            throw new AdminNotFoundException(adminId);
        }

        admin.setStatus(Status.BLOCKED);
        admin.setUpdatedAt(Instant.now());
        adminHashMap.put(admin.getId(), admin);

        return admin;
    }
}
