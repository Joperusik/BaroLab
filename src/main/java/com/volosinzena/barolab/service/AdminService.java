package com.volosinzena.barolab.service;

import com.volosinzena.barolab.service.model.Admin;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    List<Admin> getAllAdmins();

    Admin createAdmin(Admin admin);

    Admin getAdminById(UUID adminId);

    Admin activateAdmin(UUID adminId);

    Admin blockAdmin(UUID adminId);
}
