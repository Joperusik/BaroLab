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
import java.util.*;

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

    @Override
    public List<Admin> getAllAdmins() {
        List<AdminEntity> adminEntitieyList = adminRepository.findAll();

        return adminEntitieyList.stream().map(adminEntity -> adminMapper.toDomain(adminEntity))
                .toList();
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
        log.info("Get Admin Request");

        Optional<AdminEntity> optionalEntity = adminRepository.findById(adminId);

        if (optionalEntity.isEmpty()) {
            throw new AdminNotFoundException(adminId);
        }

        log.info("Successfully retrieved Admin");
        AdminEntity adminEntity = optionalEntity.get();
        return adminMapper.toDomain(adminEntity);
    }

    @Override
    public Admin activateAdmin(UUID adminId) {
        log.info("Activate Admin Request");
        Optional<AdminEntity> optionalEntity = adminRepository.findById(adminId);
        if (optionalEntity == null) {
            throw new AdminNotFoundException(adminId);
        }

        AdminEntity adminEntity = optionalEntity.get();
        adminEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.ACTIVE);
        adminEntity.setUpdatedAt(Instant.now());

        AdminEntity saveAdminEntity = adminRepository.save(adminEntity);

        log.info("Successfully activated admin");

        return adminMapper.toDomain(saveAdminEntity);
    }

    @Override
    public Admin blockAdmin(UUID adminId) {
        log.info("Block Admin Request");
        Optional<AdminEntity> optionalEntity = adminRepository.findById(adminId);
        if (optionalEntity == null) {
            throw new AdminNotFoundException(adminId);
        }

        AdminEntity adminEntity = optionalEntity.get();
        adminEntity.setStatus(com.volosinzena.barolab.repository.entity.Status.BLOCKED);
        adminEntity.setUpdatedAt(Instant.now());

        AdminEntity saveAdminEntity = adminRepository.save(adminEntity);

        log.info("Successfully blocked admin");

        return adminMapper.toDomain(saveAdminEntity);
    }
}
