package com.volosinzena.barolab.service;

import com.volosinzena.barolab.exception.AdminAlreadyExistsException;
import com.volosinzena.barolab.exception.AdminNotFoundException;
import com.volosinzena.barolab.mapper.AdminMapper;
import com.volosinzena.barolab.repository.AdminRepository;
import com.volosinzena.barolab.repository.entity.AdminEntity;
import com.volosinzena.barolab.repository.entity.Role;
import com.volosinzena.barolab.service.model.Admin;
import com.volosinzena.barolab.service.model.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    private AdminRepository adminRepository;
    private AdminMapper adminMapper;

    @Autowired
    public AdminServiceImpl(AdminRepository adminRepository, AdminMapper adminMapper) {
        this.adminRepository = adminRepository;
        this.adminMapper = adminMapper;
    }

    private final HashMap<UUID, Admin> adminHashMap = new HashMap<>();

    @Override
    public List<Admin> getAllAdmins() {
        return adminHashMap.values().stream().toList();
    }

    @Override
    public Admin createAdmin(String login, String password) {
        log.info("Create Admin Request");

        Optional<AdminEntity> optionalAdmin = adminRepository.findByLogin(login);

        if (optionalAdmin.isPresent()) {
            throw new AdminAlreadyExistsException(login);
        }

        AdminEntity adminEntity = new AdminEntity();

        adminEntity.setLogin(login);
        adminEntity.setPassword(password);
        Instant now = Instant.now();
        adminEntity.setCreatedAt(now);
        adminEntity.setUpdatedAt(now);
        adminEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        adminEntity.setRole(Role.ADMIN);


        AdminEntity saveAdminEntity = adminRepository.save(adminEntity);

        log.info("Successfully created admin");

        return adminMapper.toDomain(saveAdminEntity);
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
