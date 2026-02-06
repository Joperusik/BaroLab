package com.volosinzena.barolab.controller;

import com.volosinzena.barolab.controller.dto.AdminDto;
import com.volosinzena.barolab.mapper.AdminMapper;
import com.volosinzena.barolab.service.AdminService;
import com.volosinzena.barolab.service.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AdminController {

    private final AdminService adminService;
    private final AdminMapper adminMapper;

    @Autowired
    public AdminController(AdminService adminService, AdminMapper adminMapper) {
        this.adminService = adminService;
        this.adminMapper = adminMapper;
    }

    @PostMapping("/admins")
    public ResponseEntity<AdminDto> createAdmin(@RequestBody AdminDto adminDto) {
        Admin admin = adminMapper.toDomain(adminDto);
        Admin createdAdmin = adminService.createAdmin(admin);
        AdminDto responseDto = adminMapper.toDto(createdAdmin);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/admins")
    public ResponseEntity<List<AdminDto>> getAdmins() {
        List<Admin> adminList = adminService.getAllAdmins();
        List<AdminDto> adminDtoList = adminList.stream().map(adminMapper::toDto).toList();
        return ResponseEntity.ok(adminDtoList);
    }

    @GetMapping("/admin/{adminId}")
    public ResponseEntity<AdminDto> getAdmin(@PathVariable UUID adminId) {
        Admin admin = adminService.getAdminById(adminId);
        AdminDto responseDto = adminMapper.toDto(admin);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/admin/{adminId}/activate")
    public ResponseEntity<AdminDto> activateAdmin(@PathVariable UUID adminId) {
        Admin admin = adminService.activateAdmin(adminId);
        AdminDto responseDto = adminMapper.toDto(admin);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/admin/{adminId}/block")
    public ResponseEntity<AdminDto> blockAdmin(@PathVariable UUID adminId) {
        Admin admin = adminService.blockAdmin(adminId);
        AdminDto responseDto = adminMapper.toDto(admin);
        return ResponseEntity.ok(responseDto);
    }
}
